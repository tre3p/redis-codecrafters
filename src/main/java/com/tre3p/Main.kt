package com.tre3p

import com.tre3p.config.PersistenceConfig
import com.tre3p.handler.HandlerProvider
import com.tre3p.handler.HandlerRouter
import com.tre3p.handler.handlers.*
import com.tre3p.resp.RESPDecoder
import com.tre3p.resp.RESPEncoder
import com.tre3p.server.ConcurrentTcpServer
import com.tre3p.storage.InMemoryKeyValueStorage

private const val TCP_PORT = 6379

fun main(args: Array<String>) {
    prepareAndLaunchServer(args)
}

fun prepareAndLaunchServer(args: Array<String>) {
    val cliArgs = parseCliArguments(args)

    val mainRequestProcessor = MainRequestProcessor(
        RESPDecoder(),
        RESPEncoder(),
        HandlerRouter(buildHandlerProvider(cliArgs))
    )

    val redisServer = ConcurrentTcpServer(TCP_PORT, mainRequestProcessor::processRequest)
    redisServer.launchServer()
}

private fun buildPersistenceConfig(args: Map<String, List<String>>): PersistenceConfig {
    return PersistenceConfig(
        dirName = args["--dir"]?.first(),
        dbFileName = args["--dbfilename"]?.first()
    )
}

private fun buildHandlerProvider(args: Map<String, List<String>>): HandlerProvider {
    val echoHandler = EchoHandler()
    val pingHandler = PingHandler()

    val kvStorage = InMemoryKeyValueStorage()
    val getHandler = GetHandler(kvStorage)
    val setHandler = SetHandler(kvStorage)

    val persistenceConfig = buildPersistenceConfig(args)
    val configHandler = ConfigHandler(persistenceConfig)

    return HandlerProvider(mapOf(
        "echo" to echoHandler,
        "ping" to pingHandler,
        "get" to getHandler,
        "set" to setHandler,
        "config" to configHandler
    ))
}

fun parseCliArguments(args: Array<String>): Map<String, List<String>> {
    return args.fold(Pair(emptyMap<String, List<String>>(), "")) { (map, lastKey), elem ->
        if (elem.startsWith("-"))  Pair(map + (elem to emptyList()), elem)
        else Pair(map + (lastKey to map.getOrDefault(lastKey, emptyList()) + elem), lastKey)
    }.first
}
