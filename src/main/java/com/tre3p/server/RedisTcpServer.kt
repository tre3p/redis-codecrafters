package com.tre3p.server

import org.apache.logging.log4j.kotlin.Logging
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.ServerSocket
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class RedisTcpServer(
    private val port: Int,
    private val handlerFunc: (InputStream, OutputStream) -> (Unit)
) : Logging {

    private val defaultThreadPool = Executors.newVirtualThreadPerTaskExecutor()
    private var serverSocket: ServerSocket? = null

    fun launchServer() {
        serverSocket = ServerSocket(port).also { it.reuseAddress = true }
        thread { launchRequestListener() }
    }

    private fun launchRequestListener() {
        serverSocket ?: throw Exception("Redis TCP server isn't launched!")

        while (!Thread.currentThread().isInterrupted) {
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