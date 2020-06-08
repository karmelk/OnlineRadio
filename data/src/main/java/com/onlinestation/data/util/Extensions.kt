package com.onlinestation.data.util

import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.entities.RadioException
import java.lang.Exception
import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.gendermodels.ResponseObjectGenre
import com.onlinestation.entities.responcemodels.stationmodels.ResponseObjectStation
import com.onlinestation.entities.responcemodels.stationmodels.StationItem
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal
import retrofit2.Response

suspend fun <R> makeApiCall(
    call: suspend () -> Result<R>,
    errorMessage: Int = 4567
) = try {
    call()
} catch (e: Exception) {
    Result.Error(RadioException<Nothing>(errorMessage))
}

fun <R> analyzeResponseGenre(
    response: Response<ResponseObjectGenre<R>>
): Result<MutableList<R>> {
    when (response.code()) {
        200 -> {
            val responseBody = response.body()
            return responseBody?.response?.data?.genrelist?.genre?.let {
                return Result.Success(it)
            } ?: Result.Error(RadioException<Nothing>(response.code()))
        }

        else -> {
            return Result.Error(RadioException<Nothing>(response.code()))
        }
    }
}


fun <R> analyzeResponseStation(
    response: Response<ResponseObjectStation<R>>
): Result<MutableList<R>> {
    when (response.code()) {
        200 -> {
            val responseBody = response.body()
            return responseBody?.response?.data?.stationlist?.station?.let {
                return Result.Success(it)
            } ?: Result.Error(RadioException<Nothing>(response.code()))
        }

        else -> {
            return Result.Error(RadioException<Nothing>(response.code()))
        }
    }
}
