package com.onlinestation.entities.responcemodels.stationmodels

import com.squareup.moshi.Json

data class ResponseStationObject<R>(
    @Json(name = "stationlist")
    val stationlist: ResponseStationList<R>?

)