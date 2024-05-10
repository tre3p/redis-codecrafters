package com.tre3p.server

import com.tre3p.resp.RESPDecoder
import org.apache.logging.log4j.kotlin.Logging
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.ServerSocket
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class RedisTcpServer(
    private val port: Int
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
                    println(RESPDecoder().decode(inputStream))
                    val outputStream = clientSocket.getOutputStream()
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream))

                    var readLine = bufferedReader.readLine()
                    while (readLine != null) {
                        println(readLine)
                        println(RESPDecoder().decode(inputStream))
                        if (readLine.lowercase() == "ping") {
                            outputStream.write("+PONG\r\n".toByteArray())
                        }
                        readLine = bufferedReader.readLine()
                    }
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