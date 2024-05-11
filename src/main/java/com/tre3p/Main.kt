package com.tre3p

import com.tre3p.handler.HandlerRouter
import com.tre3p.resp.RESPDecoder
import com.tre3p.resp.RESPEncoder
import com.tre3p.server.RedisTcpServer

private const val TCP_PORT = 6379

fun main() {
    val mainRequestProcessor = MainRequestProcessor(
        RESPDecoder(),
        RESPEncoder(),
        HandlerRouter()
    )

    val redisServer = RedisTcpServer(TCP_PORT, mainRequestProcessor::processRequest)
    redisServer.launchServer()
}
