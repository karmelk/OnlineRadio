package com.nextidea.onlinestation.data.entities

import com.nextidea.onlinestation.data.util.RequestError

data class RadioException(
    val errorCode: Int,
    val errorBody: RequestError? = null,
    val errorMessage: String? = null
): Exception(errorMessage)