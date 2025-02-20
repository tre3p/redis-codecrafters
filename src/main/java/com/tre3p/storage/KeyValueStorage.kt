package com.tre3p.storage

interface KeyValueStorage {
    operator fun get(key: Any): Any?

    operator fun set(
        key: Any,
        value: Any,
    )

    fun remove(key: Any)
}
