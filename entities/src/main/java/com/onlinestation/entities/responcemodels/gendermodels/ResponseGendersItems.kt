package com.onlinestation.entities.responcemodels.gendermodels

import com.squareup.moshi.Json

data class ResponseGendersItems<R>(
    @Json(name = "genre")
    val genre: MutableList<R>?

)