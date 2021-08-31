package com.onlinestation.data.entities

data class RadioException<ErrorBody>(val errorCode: Int, val errorBody: ErrorBody? = null, val errorMessage:String?=null)