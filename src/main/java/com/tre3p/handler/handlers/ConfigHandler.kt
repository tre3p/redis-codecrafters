package com.tre3p.handler.handlers

import com.tre3p.config.PersistenceConfig
import com.tre3p.resp.types.BulkString
import com.tre3p.resp.types.RESPArray
import com.tre3p.resp.types.RESPDataType
import com.tre3p.resp.types.SimpleString

class ConfigHandler(
    val persistenceConfig: PersistenceConfig,
) : Handler {
    override val commandName: String = "config"

    override fun handle(args: List<*>): RESPDataType<*> {
        if (args.size < 3) return SimpleString("Unexpected args size for CONFIG command")

        val commandType = args[1].toString().lowercase()
        val paramName = args[2].toString().lowercase()

        var responseArray: RESPArray? = null
        when (commandType) {
            "get" -> {
                handleGetConfig(paramName)?.let {
                    responseArray = RESPArray(listOf(BulkString(paramName), BulkString(it)))
                }
            }

            else -> SimpleString("Unknown command ${args[1]} for CONFIG command")
        }

        return responseArray ?: SimpleString("Config value ${args[2]} doesn't exists")
    }

    private fun handleGetConfig(paramName: String): String? =
        when (paramName) {
            "dbfilename" -> persistenceConfig.dbFileName
            "dir" -> persistenceConfig.dirName
            else -> null
        }
}
