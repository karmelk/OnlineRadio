package com.kmstore.onlinestation.data.entities.request

data class QuerySecondaryGenreBody(
    private val _parentId: Int,
    private val _apiKey: String,
    private val _dataFormat: String
) {
    val genderId: Int
        get() = _parentId
    val apiKey: String
        get() = _apiKey
    val dataFormat: String
        get() = _dataFormat
}