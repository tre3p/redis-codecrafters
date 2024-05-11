package com.tre3p.handler.handlers

import com.tre3p.model.ExpiryValueWrapper
import com.tre3p.resp.types.BulkString
import com.tre3p.resp.types.RESPDataType
import com.tre3p.resp.types.SimpleString
import com.tre3p.storage.KeyValueStorage

class GetHandler(
    val keyValueStorage: KeyValueStorage
): Handler {

    private val NULL_BULK_STRING = BulkString(-1, "")

    // Currently treats all values as strings
    override fun handle(args: List<*>): RESPDataType {
        if (args.size < 2) return SimpleString("Unexpected args size for GET command")
        val value = keyValueStorage.getValue(args[1]!!)

        if (value == null) {
            return NULL_BULK_STRING
        }

        if (value is ExpiryValueWrapper<*>) {
            val currentTime = System.currentTimeMillis()
            return if ((value.addedMs.toInt() != -1 && value.expiresInMs.toInt() != -1) && value.addedMs + value.expiresInMs < currentTime) {
                NULL_BULK_STRING
            } else {
                BulkString(value.value.toString().length.toLong(), value.value.toString())
            }
        }

        return BulkString(value.toString().length.toLong(), value.toString())
    }
}