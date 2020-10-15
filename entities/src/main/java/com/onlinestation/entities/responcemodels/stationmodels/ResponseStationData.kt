package com.onlinestation.entities.responcemodels.stationmodels

import com.squareup.moshi.Json

data class ResponseStationData<R>(
        @Json(name = "statusCode")
        val statusCode: Int,
        @Json(name = "statusText")
        val statusText: String,
        @Json(name = "data")
        val data: ResponseStationObject<R>?
)