package com.onlinestation.data.dataservice.apiservice

import com.onlinestation.entities.responcemodels.gendermodels.ResponseObjectGenre
import com.onlinestation.entities.responcemodels.gendermodels.server.GenderItemDb
import com.onlinestation.entities.responcemodels.gendermodels.SecondaryGenreItem
import com.onlinestation.entities.responcemodels.stationmodels.ResponseObjectStation
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemResponse

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AllApiService {

    @GET("/genre/primary")
    suspend fun getPrimaryGenreList(
        @Query("k") key: String,
        @Query("f") format: String
    ): Response<ResponseObjectGenre<GenderItemDb>>


    @GET("/genre/secondary")
    suspend fun getSecondaryGenreList(
        @Query("parentid") genreParentId: Int,
        @Query("k") key: String,
        @Query("f") format: String
    ): Response<ResponseObjectGenre<SecondaryGenreItem>>

    @GET("/station/randomstations")
    suspend fun getRandomStationList(
        @Query("k") key: String,
        @Query("mt") stationFormat: String,
        @Query("limit") limit: Int,
        @Query("f") format: String
    ): Response<ResponseObjectStation<StationItemResponse>>

    @GET("/station/advancedsearch")
    suspend fun getStationListByGenreId(
        @Query("genre_id") genreId: Int,
        @Query("limit") limit: Int,
        @Query("f") format: String,
        @Query("k") key: String
    ): Response<ResponseObjectStation<StationItemResponse>>

    @GET("/station/nowplaying")
    suspend fun getSearchStationList(
        @Query("ct") ct: String,
        @Query("f") format: String,
        @Query("limit") limit: Int,
        @Query("genre") genre: String,
        @Query("k") key: String
    ): Response<ResponseObjectStation<StationItemResponse>>
    @GET("/station/nowplaying")
    suspend fun getSearchStationWithoutStationList(
        @Query("ct") ct: String,
        @Query("f") format: String,
        @Query("limit") limit: Int,
        @Query("k") key: String
    ): Response<ResponseObjectStation<StationItemResponse>>
}
