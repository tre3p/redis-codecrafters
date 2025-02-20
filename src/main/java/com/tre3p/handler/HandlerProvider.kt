package com.tre3p.handler

import com.tre3p.handler.handlers.Handler

class HandlerProvider(
    val commandHandlers: List<Handler>,
) {
    fun getHandler(command: String) = commandHandlers.find { it.commandName == command }
}
