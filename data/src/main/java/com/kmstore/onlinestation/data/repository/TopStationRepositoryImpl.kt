package com.kmstore.onlinestation.data.repository

import android.content.Context
import com.kmstore.onlinestation.data.dataservice.apiservice.AllApiService
import com.kmstore.onlinestation.data.entities.RadioException
import com.kmstore.onlinestation.data.entities.request.QueryTopStationBody
import com.kmstore.onlinestation.data.util.analyzeResponse
import com.kmstore.onlinestation.data.util.makeApiCall
import com.kmstore.onlinestation.data.entities.stationmodels.StationItemResponse
import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.data.util.NO_INTERNET_CONNECTION
import com.kmstore.onlinestation.data.util.hasNetwork

class TopStationRepositoryImpl(
    private val allApiService: AllApiService,
    private val context: Context
) : TopStationRepository {

    override suspend fun getTopStations(queryBody: QueryTopStationBody):
            DataResult<List<StationItemResponse>> {
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
            DataResult.Error(RadioException(NO_INTERNET_CONNECTION))
        }
    }

}