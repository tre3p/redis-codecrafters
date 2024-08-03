package com.tre3p.handler.handlers

import com.tre3p.model.ExpiryValueWrapper
import com.tre3p.resp.types.BulkString
import com.tre3p.resp.types.RESPDataType
import com.tre3p.resp.types.SimpleString
import com.tre3p.storage.KeyValueStorage

class GetHandler(
    val keyValueStorage: KeyValueStorage,
) : Handler {
    private val nullBulkString = BulkString(null)

    override fun handle(args: List<*>): RESPDataType {
        if (args.size < 2) return SimpleString("Unexpected args size for GET command")
        val key = args[1]!!
        val storageValue = keyValueStorage.getValue(key)

        if (storageValue == null) {
            return nullBulkString
        }

        if (storageValue is ExpiryValueWrapper<*>) {
            val currentTime = System.currentTimeMillis()

            return if (storageValue.expiryTime.toInt() != -1 && storageValue.expiryTime < currentTime) {
                keyValueStorage.removeKey(key)
                nullBulkString
            } else {
                BulkString(storageValue.value.toString())
            }
        }

        return BulkString(storageValue.toString())
    }
}
