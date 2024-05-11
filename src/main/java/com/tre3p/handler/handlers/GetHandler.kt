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

    override fun handle(args: List<*>): RESPDataType {
        if (args.size < 2) return SimpleString("Unexpected args size for GET command")
        val key = args[1]!!
        val value = keyValueStorage.getValue(key)

        if (value == null) {
            return NULL_BULK_STRING
        }

        if (value is ExpiryValueWrapper<*>) {
            val currentTime = System.currentTimeMillis()
            return if (value.expiryTime.toInt() != -1 && value.expiryTime < currentTime) {
                keyValueStorage.removeKey(key)
                NULL_BULK_STRING
            } else {
                BulkString(value.value.toString().length.toLong(), value.value.toString())
            }
        }

        return BulkString(value.toString().length.toLong(), value.toString())
    }
}