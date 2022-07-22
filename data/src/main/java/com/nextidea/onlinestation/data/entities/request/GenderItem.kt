package com.nextidea.onlinestation.data.entities.request

import com.nextidea.onlinestation.data.entities.DiffUtilModel

data class GenderItem(
    val stationcount: Int,
    val name: String,
    val isoCode: String
): DiffUtilModel<Int>{

    override val id: Int = this.hashCode()
}