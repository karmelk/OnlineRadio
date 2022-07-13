package com.nextidea.onlinestation.data.util

import com.nextidea.onlinestation.data.entities.*
import retrofit2.Response

suspend fun <R> makeApiCall(
    call: suspend () -> DataResult<R>,
    errorMessage: Int = 4567
) = try {
    call()
} catch (e: Exception) {
    DataResult.Error(RadioException(errorMessage))
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
                    DataResult.Error(RadioException(response.code()))
                }
            } ?: DataResult.Error(RadioException(response.code()))
        }

        else -> {
            return DataResult.Error(RadioException(response.code()))
        }
    }
}

fun <Model> newAnalyzeResponse(
    response: Response<Model>
): DataResult<Model> {
    when {
        response.isSuccessful -> {
            val responseBody = response.body()
            return responseBody?.let {
                DataResult.Success(it)
            } ?: DataResult.Error(RadioException(response.code()))
        }

        else -> {
            return DataResult.Error(RadioException(response.code()))
        }
    }
}


