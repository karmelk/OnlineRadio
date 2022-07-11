package com.nextidea.onlinestation.data.repository

import android.content.Context
import com.nextidea.onlinestation.data.dataservice.apiservice.AllApiService
import com.nextidea.onlinestation.data.entities.RadioException
import com.nextidea.onlinestation.data.entities.request.QueryTopStationBody
import com.nextidea.onlinestation.data.util.analyzeResponse
import com.nextidea.onlinestation.data.util.makeApiCall
import com.nextidea.onlinestation.data.entities.stationmodels.StationItemResponse
import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.data.util.NO_INTERNET_CONNECTION
import com.nextidea.onlinestation.data.util.hasNetwork
import org.koin.core.annotation.Single

@Single
internal class TopStationRepositoryImpl(
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