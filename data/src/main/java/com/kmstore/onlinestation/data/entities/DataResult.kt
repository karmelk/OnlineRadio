package com.kmstore.onlinestation.data.entities

sealed class DataResult<out S> {
    data class Success<S>(val data: S) : DataResult<S>()
    data class Error(val errors: RadioException<Throwable>) : DataResult<Nothing>()
}