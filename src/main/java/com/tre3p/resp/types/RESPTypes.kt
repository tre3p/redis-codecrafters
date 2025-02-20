package com.tre3p.resp.types

interface RESPDataType<T> {
    val data: T
}

data class SimpleString(
    override val data: String,
) : RESPDataType<String>

data class BulkString(
    override val data: String?,
) : RESPDataType<String?> {
    val length: Int = data?.length ?: -1
}

data class RESPArray(
    override val data: List<RESPDataType<*>>,
) : RESPDataType<List<RESPDataType<*>>>
