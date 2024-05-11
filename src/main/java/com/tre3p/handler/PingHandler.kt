package com.tre3p.handler

import com.tre3p.resp.types.SimpleString

class PingHandler : Handler {
    override fun handle(args: List<*>): SimpleString {
        return SimpleString("PONG")
    }
}