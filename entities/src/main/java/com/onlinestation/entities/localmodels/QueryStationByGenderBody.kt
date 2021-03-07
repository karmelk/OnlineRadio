package com.onlinestation.entities.localmodels

data class QueryStationByGenderBody(
    private val _method: String,
    private val _apiKey: String,
    private val _offset: Int,
    private val _limit: Int,
    private val _genre_id: Long
) {
    val method: String
        get() = _method
    val apiKey: String
        get() = _apiKey
    val offset: Int
        get() = _offset
    val limit: Int
        get() = _limit
    val genre_id: Long
        get() = _genre_id
}