package com.tre3p.model

data class ExpiryValueWrapper<T>(
    val value: T,
    var expiresInMs: Long = -1,
    var addedMs: Long = -1
)