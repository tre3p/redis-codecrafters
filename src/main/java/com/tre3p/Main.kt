package com.tre3p

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket

fun main() {
    var serverSocket: ServerSocket? = null
    var socketClient: Socket? = null
    val port = 6379

    serverSocket = ServerSocket(port)
    serverSocket.reuseAddress = true
    socketClient = serverSocket.accept()
    val inputStream = socketClient.getInputStream()
    val socketStreamBr = BufferedReader(InputStreamReader(inputStream))

    var readLine: String? = socketStreamBr.readLine()
    while (readLine != null) {
        if (readLine.lowercase() == "ping") {
            socketClient.getOutputStream().write("+PONG\r\n".toByteArray())
        }
        readLine = socketStreamBr.readLine()
    }
}