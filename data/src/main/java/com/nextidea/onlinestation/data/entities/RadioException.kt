package com.nextidea.onlinestation.data.entities

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.nextidea.onlinestation.data.util.RequestError

data class RadioException(
    val errorCode: Int,
    val errorBody: RequestError? = null,
    val errorMessage: String? = null
): Exception(errorMessage){
    companion object {
        fun CombinedLoadStates.toCallException() = (((refresh as LoadState.Error).error) as? RadioException)
    }
}