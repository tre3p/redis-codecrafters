package com.tre3p.handler.handlers

import com.tre3p.resp.types.BulkString
import com.tre3p.resp.types.RESPDataType
import com.tre3p.resp.types.SimpleString
import com.tre3p.storage.KeyValueStorage

class GetHandler(
    val keyValueStorage: KeyValueStorage
): Handler {
    // Currently treats all values as strings
    override fun handle(args: List<*>): RESPDataType {
        if (args.size < 2) return SimpleString("Unexpected args size for GET command")
        val value = keyValueStorage.getValue(args[1]!!)?.toString()

        if (value == null) {
            return BulkString(-1, "")
        }

        return BulkString(value.length.toLong(), value)
    }
}