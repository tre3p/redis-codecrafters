package com.tre3p.resp.types

open class RESPDataType

data class SimpleString(val data: String) : RESPDataType()
data class BulkString(val data: String?): RESPDataType() {
    val length: Int = data?.length ?: -1
}
data class RESPArray(val elements: List<RESPDataType>): RESPDataType()