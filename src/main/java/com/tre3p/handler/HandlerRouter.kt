package com.tre3p.handler

import com.tre3p.resp.types.SimpleString
import org.apache.logging.log4j.kotlin.Logging

class HandlerRouter(
    private val handlerProvider: HandlerProvider,
) : Logging {
    fun route(args: List<*>): Any {
        if (args.isEmpty()) {
            logger.debug("No arguments provided, nothing to do..")
            return SimpleString("No arguments provided")
        }

        val commandType = args[0].toString().lowercase()
        val commandHandler = handlerProvider.getHandler(commandType)
        if (commandHandler == null) {
            logger.debug("No handler found for command $commandType")
            return SimpleString("No handler found for command $commandType")
        }

        return commandHandler.handle(args)
    }
}
