package com.tre3p.storage

class InMemoryKeyValueStorage : KeyValueStorage {
    private val storage = HashMap<Any, Any>()

    override operator fun get(key: Any): Any? = storage[key]

    override operator fun set(
        key: Any,
        value: Any,
    ) {
        storage[key] = value
    }

    override fun remove(key: Any) {
        storage.remove(key)
    }
}
