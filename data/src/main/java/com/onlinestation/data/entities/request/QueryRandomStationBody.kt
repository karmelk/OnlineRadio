package com.onlinestation.data.entities.request

data class QueryRandomStationBody(
    private val _apiKey: String,
    private val _stationFormat: String,
    private val _limit: Int,
    private val _dataFormat: String
) {
    val apiKey: String
        get() = _apiKey
    val stationFormat: String
        get() = _stationFormat
    val limit: Int
        get() = _limit
    val dataFormat: String
        get() = _dataFormat
}