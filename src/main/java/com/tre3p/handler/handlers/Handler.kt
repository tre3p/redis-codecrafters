package com.tre3p.handler.handlers

import com.tre3p.resp.types.RESPDataType

interface Handler {
    fun handle(args: List<*>): RESPDataType
}
