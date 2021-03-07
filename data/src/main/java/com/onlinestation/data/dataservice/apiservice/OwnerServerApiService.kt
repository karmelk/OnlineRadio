package com.onlinestation.data.dataservice.apiservice

import com.onlinestation.entities.responcemodels.ParentResponse
import com.onlinestation.entities.responcemodels.gendermodels.server.ResponseGender
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemResponse

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OwnerServerApiService {

    @GET("api/api.php")
    suspend fun getPrimaryOwnerGenreList(
        @Query("method") method: String,
        @Query("api_key") key: String
    ): Response<ParentResponse<ResponseGender>>

    @GET("api/api.php")
    suspend fun getStationsByGenreId(
        @Query("method") method: String,
        @Query("api_key") key: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("genre_id") id: Long
    ): Response<ParentResponse<StationItemResponse>>
}
