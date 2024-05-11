package com.tre3p.handler.handlers

import com.tre3p.resp.types.RESPDataType
import com.tre3p.resp.types.SimpleString
import com.tre3p.storage.KeyValueStorage

class SetHandler(
    val kvStorage: KeyValueStorage
): Handler {
    override fun handle(args: List<*>): RESPDataType {
        if (args.size < 3) return SimpleString("Unexpected args count for SET command")
        val key = args[1]!!
        val value = args[2]!!
        kvStorage.putValue(key, value)

        return SimpleString("OK")
    }
}