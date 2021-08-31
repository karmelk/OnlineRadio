package com.onlinestation.data.util

import com.onlinestation.data.entities.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import retrofit2.Response

suspend fun <R> makeApiCall(
    call: suspend () -> Result<R>,
    errorMessage: Int = 4567
) = try {
    call()
} catch (e: Exception) {
    Result.Error(RadioException(errorMessage,errorBody = e))
}

fun <Model> analyzeResponse(
    response: Response<ParentResponse<Model>>
): Result<List<Model>> {
    when (response.code()) {
        200 -> {
            val responseBody = response.body()
            return responseBody?.let {
                if (it.status == 200) {
                    Result.Success(it.datas)
                } else {
                    Result.Error(RadioException(response.code(),Throwable()))
                }
            } ?: Result.Error(RadioException(response.code(),Throwable("Response body is null")))
        }

        else -> {
            return Result.Error(RadioException(response.code(),Throwable(response.message())))
        }
    }
}


