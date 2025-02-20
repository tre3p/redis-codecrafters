package com.tre3p.handler

import com.tre3p.resp.types.SimpleString
import org.apache.logging.log4j.kotlin.Logging

class HandlerRouter(
    private val handlerProvider: HandlerProvider,
) : Logging {
    fun route(args: List<*>?) =
        args?.let {
            if (args.isEmpty()) {
                logger.debug("No arguments provided, nothing to do..")
                return SimpleString("No arguments provided")
            }

            val commandType = args[0].toString().lowercase()
            handlerProvider
                .getHandler(commandType)
                ?.handle(args)
                ?: SimpleString("No handler found for command $commandType")
        } ?: ByteArray(0)
}
