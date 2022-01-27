package com.kmstore.onlinestation.data.util

import com.kmstore.onlinestation.data.entities.*
import retrofit2.Response

suspend fun <R> makeApiCall(
    call: suspend () -> DataResult<R>,
    errorMessage: Int = 4567
) = try {
    call()
} catch (e: Exception) {
    DataResult.Error(RadioException(errorMessage,errorBody = e))
}

fun <Model> analyzeResponse(
    response: Response<ParentResponse<Model>>
): DataResult<List<Model>> {
    when (response.code()) {
        200 -> {
            val responseBody = response.body()
            return responseBody?.let {
                if (it.status == 200) {
                    DataResult.Success(it.datas)
                } else {
                    DataResult.Error(RadioException(response.code(),Throwable()))
                }
            } ?: DataResult.Error(RadioException(response.code(),Throwable("Response body is null")))
        }

        else -> {
            return DataResult.Error(RadioException(response.code(),Throwable(response.message())))
        }
    }
}


