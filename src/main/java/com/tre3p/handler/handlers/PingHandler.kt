package com.tre3p.handler.handlers

import com.tre3p.resp.types.RESPDataType
import com.tre3p.resp.types.SimpleString

class PingHandler : Handler {
    override val commandName: String = "ping"

    override fun handle(args: List<*>): SimpleString = SimpleString("PONG")
}
