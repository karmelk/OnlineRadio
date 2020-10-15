package com.onlinestation.entities.responcemodels.gendermodels

import com.squareup.moshi.Json

data class ResponseObjectGenre<R>(
        @Json(name = "response")
        val response: ResponseGenderData<R>?
)