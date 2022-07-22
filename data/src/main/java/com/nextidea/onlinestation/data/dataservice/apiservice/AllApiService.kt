package com.nextidea.onlinestation.data.dataservice.apiservice

import com.nextidea.onlinestation.data.entities.ParentResponse
import com.nextidea.onlinestation.data.entities.gendermodels.ResponseGender
import com.nextidea.onlinestation.data.entities.stationmodels.StationItemResponse

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AllApiService {

    @GET("countries")
    suspend fun getGenreList(
        @Query("limit") per_page: Int?,
        @Query("offset") page: Int?
    ): Response<List<ResponseGender>>

    @GET("api/api.php")
    suspend fun getStationsByGenreId(
        @Query("method") method: String,
        @Query("api_key") key: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("genre_id") id: Long
    ): Response<ParentResponse<StationItemResponse>>

    @GET("api/api.php")
    suspend fun getTopStation(
        @Query("method") method: String,
        @Query("api_key") key: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("is_feature") id: Byte
    ): Response<ParentResponse<StationItemResponse>>

    @GET("api/api.php")
    suspend fun getSearchStationList(
        @Query("method") method: String,
        @Query("api_key") key: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("q") id: String
    ): Response<ParentResponse<StationItemResponse>>
}
