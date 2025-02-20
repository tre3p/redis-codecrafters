package com.tre3p

import com.tre3p.resp.RESPDecoder
import com.tre3p.resp.RESPEncoder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException

open class BaseIntegrationTest(
    args: Map<String, List<String>> = mapOf(),
) {
    private val tcpServer = prepareServer(args)
    protected val respEncoder = RESPEncoder()
    protected val respDecoder = RESPDecoder()

    @BeforeEach
    fun beforeEach() {
        tcpServer.launchServer()
    }

    @AfterEach
    fun afterEach() {
        tcpServer.stopServer()
    }

    fun getServerConnection(): Socket {
        val clientSocket = Socket()
        clientSocket.connect(InetSocketAddress(6379))
        return clientSocket
    }

    protected fun Socket.sendServerMessage(msg: ByteArray) {
        this.getOutputStream().write(msg)
    }

    protected fun Socket.awaitServerMessage(timeoutMs: Int = 100): Any? {
        this.soTimeout = timeoutMs
        val buffer = ByteArray(1024)
        val byteArrayOutputStream = ByteArrayOutputStream()

        try {
            val inputStream = this.getInputStream()
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
            }
        } catch (_: SocketTimeoutException) {
            // Do nothing, return from method
        }

        return respDecoder.decode(ByteArrayInputStream(byteArrayOutputStream.toByteArray()))
    }
}
