package com.onlinestation.entities.localmodels

data class QueryStationByGenderBody(
    private val _parentId: Int,
    private val _limit: Int,
    private val _dataFormat: String,
    private val _apiKey: String
) {
    val genderId: Int
        get() = _parentId
    val limit: Int
        get() = _limit
    val dataFormat: String
        get() = _dataFormat
    val apiKey: String
        get() = _apiKey
}