package com.tre3p.handler

import org.apache.logging.log4j.kotlin.Logging

private val handlers = mapOf(
    "echo" to EchoHandler()
)

class HandlerRouter : Logging {
    fun route(args: List<*>): Any? {
        if (args.isEmpty()) {
            logger.debug("No arguments provided, nothing to do..")
            return null
        }

        val commandType = args[0].toString().lowercase()
        val commandHandler = handlers[commandType]
        if (commandHandler == null) {
            logger.debug("No handler found for command $commandType")
            return null
        }

        return commandHandler.handle(args)
    }
}