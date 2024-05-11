package com.tre3p.handler

import com.tre3p.resp.types.RESPDataType

interface Handler {
    fun handle(args: List<*>): RESPDataType
}