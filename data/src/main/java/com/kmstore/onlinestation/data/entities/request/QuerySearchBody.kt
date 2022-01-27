package com.kmstore.onlinestation.data.entities.request

data class QuerySearchBody(
    private val _method: String,
    private val _apiKey: String,
    private val _offset: Int,
    private val _limit: Int,
    private val _keyword: String
) {
    val method: String
        get() = _method
    val apiKey: String
        get() = _apiKey
    val offset: Int
        get() = _offset
    val limit: Int
        get() = _limit
    val keyword: String
        get() = _keyword

}