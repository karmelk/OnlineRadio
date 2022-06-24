package com.nextidea.onlinestation.data.entities.request

data class QueryTopStationBody(
    private val _method: String,
    private val _apiKey: String,
    private val _offset: Int,
    private val _limit: Int,
    private val _isFeature: Byte
) {
    val method: String
        get() = _method
    val apiKey: String
        get() = _apiKey
    val offset: Int
        get() = _offset
    val limit: Int
        get() = _limit
    val isFeature: Byte
        get() = _isFeature
}