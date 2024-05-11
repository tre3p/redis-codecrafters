package com.tre3p.handler.handlers

import com.tre3p.resp.types.BulkString
import java.lang.Exception

class EchoHandler : Handler {
    override fun handle(args: List<*>): BulkString {
        if (args.size < 2) throw Exception("Invalid command! ECHO command requires at least one argument")
        val echoValues = args.subList(1, args.size)
        val echoString = echoValues.joinToString(" ")
        return BulkString(echoString.length.toLong(), echoString)
    }
}