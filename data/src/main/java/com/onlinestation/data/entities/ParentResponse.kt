package com.onlinestation.data.entities


import com.squareup.moshi.Json

data class  ParentResponse<Model>(
    @Json(name = "status")
    val status: Int,
    @Json(name = "msg")
    val msg: String,
    @Json(name = "datas")
    val datas: List<Model>
)