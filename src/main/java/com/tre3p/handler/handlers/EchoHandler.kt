package com.tre3p.handler.handlers

import com.tre3p.resp.types.BulkString
import com.tre3p.resp.types.RESPDataType
import com.tre3p.resp.types.SimpleString

class EchoHandler : Handler {
    override fun handle(args: List<*>): RESPDataType {
        if (args.size < 2) return SimpleString("Unexpected args size for ECHO command")
        val echoValues = args.subList(1, args.size)
        val echoString = echoValues.joinToString(" ")
        return BulkString(echoString)
    }
}
