package com.onlinestation.entities.localmodels

import java.io.Serializable

data class QueryPrimaryGenreBody(
    private val _apiKey: String,
    private val _dataFormat: String
) {
    val apiKey: String
        get() = _apiKey
    val dataFormat: String
        get() = _dataFormat
}