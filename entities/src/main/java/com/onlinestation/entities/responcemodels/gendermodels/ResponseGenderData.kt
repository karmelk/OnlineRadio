package com.onlinestation.entities.responcemodels.gendermodels

import com.squareup.moshi.Json

data class ResponseGenderData<R>(
        @Json(name = "statusCode")
        val statusCode: Int,
        @Json(name = "statusText")
        val statusText: String,
        @Json(name = "data")
        val data: ResponseGenderList<R>?
)