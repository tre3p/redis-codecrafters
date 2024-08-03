package com.tre3p.model

data class ExpiryValueWrapper<T>(
    val value: T,
    var expiryTime: Long = -1,
)
