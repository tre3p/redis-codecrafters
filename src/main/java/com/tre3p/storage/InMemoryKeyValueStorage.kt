package com.tre3p.storage

import java.util.concurrent.ConcurrentHashMap

class InMemoryKeyValueStorage : KeyValueStorage {
    private val storage = ConcurrentHashMap<Any, Any>()

    override fun getValue(key: Any): Any? {
        return storage[key]
    }

    override fun putValue(key: Any, value: Any) {
        storage[key] = value
    }

    override fun removeKey(key: Any) {
        storage.remove(key)
    }
}