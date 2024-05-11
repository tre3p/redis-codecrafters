package com.tre3p.resp.types

open class RESPDataType

data class SimpleString(val data: String) : RESPDataType()
data class BulkString(val length: Long, val data: String): RESPDataType()