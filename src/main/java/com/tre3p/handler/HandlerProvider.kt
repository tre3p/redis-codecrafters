package com.tre3p.handler

import com.tre3p.handler.handlers.Handler

class HandlerProvider(
    val commandToHandler: Map<String, Handler>,
) {
    operator fun get(command: String): Handler? = commandToHandler[command]
}
