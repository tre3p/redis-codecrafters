package com.tre3p.handler

import com.tre3p.handler.handlers.Handler

class HandlerProvider(
    val commandToHandler: Map<String, Handler>
) {
    fun getHandler(command: String): Handler? {
        return commandToHandler[command]
    }
}