package com.onlinestation.domain.usecases

import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.data.datastore.TopStationRepository
import com.onlinestation.data.entities.Constants.Companion.API_KEY
import com.onlinestation.data.entities.Constants.Companion.METHOD_GET_RADIOS
import com.onlinestation.data.entities.Constants.Companion.RADIOS_LIMIT
import com.onlinestation.data.entities.Constants.Companion.RADIOS_OFFSET
import com.onlinestation.data.entities.RadioException
import com.onlinestation.domain.interactors.TopStationInteractor
import com.onlinestation.data.entities.Result
import com.onlinestation.data.entities.request.QueryTopStationBody
import com.onlinestation.domain.entities.StationItem
import com.onlinestation.domain.utils.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TopStationUseCase(
    private val topStationRepository: TopStationRepository,
    private val localSQLRepository: LocalSQLRepository
) : TopStationInteractor {

    override suspend operator fun invoke(): Result<List<StationItem>> {
        val result = topStationRepository.getTopStations(
            QueryTopStationBody(
                METHOD_GET_RADIOS, API_KEY, RADIOS_OFFSET,
                RADIOS_LIMIT, 1
            )
        )

        return when (result) {
            is Result.Success -> {
                val updatedItems = (result.data?.map { it.toDomain(false) }
                    ?: listOf())
                    .map { localItem ->
                        val dbItem = localSQLRepository.getItemStationDB(localItem.id)
                        if (dbItem != null) {
                            localItem.copy(isFavorite = true)
                        } else {
                            localItem
                        }
                    }

                Result.Success(updatedItems)
            }
            is Result.Error -> {
                Result.Error(RadioException(result.errors.errorCode))
            }
        }
    }
}