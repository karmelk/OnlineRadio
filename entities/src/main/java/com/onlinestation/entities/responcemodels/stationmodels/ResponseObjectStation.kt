package com.onlinestation.entities.responcemodels.stationmodels

import com.squareup.moshi.Json

data class ResponseObjectStation<R>(
        @Json(name = "response")
        val response: ResponseStationData<R>?
)