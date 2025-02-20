package com.tre3p.handler.handlers

import com.tre3p.resp.types.RESPDataType

interface Handler {
    val commandName: String

    fun handle(args: List<*>): RESPDataType<*>
}
