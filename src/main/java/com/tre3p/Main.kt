package com.tre3p

import java.net.ServerSocket
import java.net.Socket

fun main() {
    var serverSocket: ServerSocket? = null
    var socketClient: Socket? = null
    val port = 6379

    serverSocket = ServerSocket(port)
    serverSocket.reuseAddress = true
    socketClient = serverSocket.accept()
    socketClient.getOutputStream().write("+PONG\r\n".toByteArray())
    socketClient?.close()
}