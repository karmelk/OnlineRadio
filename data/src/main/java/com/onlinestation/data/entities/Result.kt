package com.onlinestation.data.entities

sealed class Result<out S> {
    data class Success<S>(val data: S?) : Result<S>()
    data class Error(val errors: RadioException<Throwable>) : Result<Nothing>()
}