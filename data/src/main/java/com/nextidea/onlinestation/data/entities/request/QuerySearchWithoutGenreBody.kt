package com.nextidea.onlinestation.data.entities.request

data class QuerySearchWithoutGenreBody(
    private val _ct: String,
    private val _dataFormat: String,
    private val _limit: Int,
    private val _apiKey: String
) {

    val ct: String
        get() = _ct
    val dataFormat: String
        get() = _dataFormat
    val limit: Int
        get() = _limit
    val apiKey: String
        get() = _apiKey

}