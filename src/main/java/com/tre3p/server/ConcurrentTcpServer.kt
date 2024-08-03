package com.tre3p.server

import org.apache.logging.log4j.kotlin.Logging
import java.io.InputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class ConcurrentTcpServer(
    private val port: Int,
    private val handlerFunc: (InputStream, OutputStream) -> (Unit),
) : Logging {
    private val defaultThreadPool = Executors.newVirtualThreadPerTaskExecutor()
    private var serverSocket: ServerSocket? = null

    fun launchServer() {
        serverSocket = ServerSocket(port).also { it.reuseAddress = true }
        thread { launchRequestListener() }
        logger.info("TCP server started")
    }

    fun stopServer() {
        serverSocket?.close()
        logger.info("Closing server socket")
    }

    private fun launchRequestListener() {
        serverSocket ?: throw Exception("TCP server isn't launched!")

        while (!Thread.currentThread().isInterrupted && !serverSocket!!.isClosed) {
            val clientSocket = serverSocket!!.accept()

            defaultThreadPool.execute {
                try {
                    logger.info("Received new request, processing..")
                    val inputStream = clientSocket.getInputStream()
                    val outputStream = clientSocket.getOutputStream()
                    handlerFunc.invoke(inputStream, outputStream)
                    logger.info("Request processed successfully!")
                } catch (e: Exception) {
                    logger.error("Exception while processing request", e)
                } finally {
                    clientSocket.close()
                }
            }
        }
    }
}
