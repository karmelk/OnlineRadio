package com.onlinestation.entities.responcemodels.stationmodels

import com.squareup.moshi.Json

data class ResponseStationList<R>(
    @Json(name = "station")
    val station: MutableList<R>?,
    @Json(name = "tunein")
    val tunein: ResponseStationTunein?
)