package com.onlinestation.data.repository

import android.content.Context
import com.onlinestation.data.dataservice.apiservice.AllApiService
import com.onlinestation.data.datastore.TopStationRepository
import com.onlinestation.data.entities.RadioException
import com.onlinestation.data.entities.request.QueryTopStationBody
import com.onlinestation.data.util.analyzeResponse
import com.onlinestation.data.util.makeApiCall
import com.onlinestation.data.entities.stationmodels.StationItemResponse
import com.onlinestation.data.entities.Result
import com.onlinestation.data.util.NO_INTERNET_CONNECTION
import com.onlinestation.data.util.hasNetwork

internal class TopStationRepositoryImpl(
    private val allApiService: AllApiService,
    private val context: Context
) : TopStationRepository {

    override suspend fun getTopStations(queryBody: QueryTopStationBody):
            Result<List<StationItemResponse>> {
        return if (context.hasNetwork()) {
            makeApiCall({
                analyzeResponse(
                    allApiService.getTopStation(
                        queryBody.method,
                        queryBody.apiKey,
                        queryBody.offset,
                        queryBody.limit,
                        queryBody.isFeature
                    )
                )
            })
        } else {
            Result.Error(RadioException(NO_INTERNET_CONNECTION))
        }
    }

}