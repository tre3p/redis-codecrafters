package com.tre3p.server

import org.apache.logging.log4j.kotlin.Logging
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.InetSocketAddress
import java.net.StandardSocketOptions
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import kotlin.concurrent.thread

class MultiplexedTcpServer(
    private val port: Int,
    private val handlerFunc: (InputStream) -> (ByteArray),
) : Logging {
    private lateinit var serverSocket: ServerSocketChannel
    private lateinit var selector: Selector

    fun stopServer() {
        logger.info("Stopping server..")
        serverSocket.close()
        selector.close()
    }

    fun launchServer() {
        selector = Selector.open()
        serverSocket =
            ServerSocketChannel.open().also {
                it.bind(InetSocketAddress(port))
                it.configureBlocking(false)
                it.register(selector, SelectionKey.OP_ACCEPT)
                it.setOption(StandardSocketOptions.SO_REUSEADDR, true)
            }

        logger.info("Starting server")
        thread { selectorLoop(selector, serverSocket) }
        logger.info("Server started!")
    }

    private tailrec fun selectorLoop(
        selector: Selector,
        serverSock: ServerSocketChannel,
    ) {
        selector.select(200)

        val selectedKeysIter = selector.selectedKeys().iterator()
        while (selectedKeysIter.hasNext()) {
            val key = selectedKeysIter.next()

            if (key.isAcceptable) {
                handleAcceptable(selector, serverSock)
            } else if (key.isReadable) {
                handleReadable((key.channel() as SocketChannel))
            }

            selectedKeysIter.remove()
        }

        selectorLoop(selector, serverSock)
    }

    private fun handleReadable(clientSocketChannel: SocketChannel) {
        val buf = ByteBuffer.allocate(4096) // TODO: check if this could be a problem (i.e client can send more than 4096 bytes?)
        val readBytes = clientSocketChannel.read(buf)

        if (readBytes == -1) {
            logger.info("No more bytes to read from client, closing connection")
            clientSocketChannel.close()
        } else {
            logger.info("Read $readBytes bytes from client, handling..")
            val response = handlerFunc.invoke(ByteArrayInputStream(buf.array()))
            clientSocketChannel.write(ByteBuffer.wrap(response))
            logger.info("Wrote ${response.size} bytes to client")
        }
    }

    private fun handleAcceptable(
        mainSelector: Selector,
        serverSocketChannel: ServerSocketChannel,
    ) {
        serverSocketChannel.accept()?.also {
            it.configureBlocking(false)
            it.register(mainSelector, SelectionKey.OP_READ)
            logger.info("New connection accepted")
        }
    }
}
