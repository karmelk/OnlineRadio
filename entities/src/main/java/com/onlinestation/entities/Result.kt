package com.onlinestation.entities

import com.onlinestation.entities.responcemodels.OwnerUserBalance

sealed class Result<out S> {
    data class Success<S>(val data: S?) : Result<S>()
    data class Error<E>(val errors: RadioException<E>) : Result<E>()
}