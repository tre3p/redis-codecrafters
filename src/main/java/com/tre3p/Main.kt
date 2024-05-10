package com.tre3p

import com.tre3p.server.RedisTcpServer

fun main() {
    val port = 6379
    val redisServer = RedisTcpServer(port)
    redisServer.launchServer()
}