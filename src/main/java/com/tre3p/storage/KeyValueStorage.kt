package com.tre3p.storage

interface KeyValueStorage {
    fun getValue(key: Any): Any?

    fun putValue(
        key: Any,
        value: Any,
    )

    fun removeKey(key: Any)
}
