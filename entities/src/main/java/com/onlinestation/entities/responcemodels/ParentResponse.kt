package com.onlinestation.entities.responcemodels


import com.squareup.moshi.Json

data class  ParentResponse<O>(
    @Json(name = "status")
    val status: Int,
    @Json(name = "msg")
    val msg: String,
    @Json(name = "datas")
    val datas: List<O>
)