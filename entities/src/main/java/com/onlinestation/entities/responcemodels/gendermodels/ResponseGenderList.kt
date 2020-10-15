package com.onlinestation.entities.responcemodels.gendermodels

import com.squareup.moshi.Json

data class ResponseGenderList<R>(
    @Json(name = "genrelist")
    val genrelist: ResponseGendersItems<R>?

)