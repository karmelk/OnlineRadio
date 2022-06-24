package com.nextidea.onlinestation.data.entities.request

data class QueryGenreBody(
    private val _apiKey: String,
    private val _method: String
) {
    val apiKey: String
        get() = _apiKey
    val method: String
        get() = _method
}