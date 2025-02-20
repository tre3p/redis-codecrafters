package com.tre3p.handler.handlers

import com.tre3p.model.ExpiryValueWrapper
import com.tre3p.resp.types.RESPDataType
import com.tre3p.resp.types.SimpleString
import com.tre3p.storage.KeyValueStorage

class SetHandler(
    val kvStorage: KeyValueStorage,
) : Handler {
    override val commandName: String = "set"

    override fun handle(args: List<*>): SimpleString {
        if (args.size < 3) return SimpleString("Unexpected args count for SET command")
        val key = args[1]!!
        val value = args[2]!!
        val expiryValueWrapper = ExpiryValueWrapper(value = value)

        if (args.size > 3) {
            val arg = args[3]!!.toString()

            if (arg.lowercase() == "px") {
                val currentTime = System.currentTimeMillis()
                val expiresIn = args[4]!!.toString().toLong()
                expiryValueWrapper.expirationTimestamp = currentTime + expiresIn
            }
        }

        kvStorage[key] = expiryValueWrapper
        return SimpleString("OK")
    }
}
