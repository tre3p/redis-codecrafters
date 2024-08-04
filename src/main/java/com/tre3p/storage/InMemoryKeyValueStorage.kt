package com.tre3p.storage

class InMemoryKeyValueStorage : KeyValueStorage {
    private val storage = HashMap<Any, Any>()

    override fun getValue(key: Any): Any? = storage[key]

    override fun putValue(
        key: Any,
        value: Any,
    ) {
        storage[key] = value
    }

    override fun removeKey(key: Any) {
        storage.remove(key)
    }
}
