package com.tre3p

import com.tre3p.handler.HandlerProvider
import com.tre3p.handler.HandlerRouter
import com.tre3p.handler.handlers.EchoHandler
import com.tre3p.handler.handlers.GetHandler
import com.tre3p.handler.handlers.PingHandler
import com.tre3p.handler.handlers.SetHandler
import com.tre3p.resp.RESPDecoder
import com.tre3p.resp.RESPEncoder
import com.tre3p.server.ConcurrentTcpServer
import com.tre3p.storage.SimpleInMemoryKeyValueStorage

private const val TCP_PORT = 6379

fun main() {
    val mainRequestProcessor = MainRequestProcessor(
        RESPDecoder(),
        RESPEncoder(),
        HandlerRouter(buildHandlerProvider())
    )

    val redisServer = ConcurrentTcpServer(TCP_PORT, mainRequestProcessor::processRequest)
    redisServer.launchServer()
}

private fun buildHandlerProvider(): HandlerProvider {
    val echoHandler = EchoHandler()
    val pingHandler = PingHandler()

    val kvStorage = SimpleInMemoryKeyValueStorage()
    val getHandler = GetHandler(kvStorage)
    val setHandler = SetHandler(kvStorage)

    return HandlerProvider(mapOf(
        "echo" to echoHandler,
        "ping" to pingHandler,
        "get" to getHandler,
        "set" to setHandler
    ))
}
