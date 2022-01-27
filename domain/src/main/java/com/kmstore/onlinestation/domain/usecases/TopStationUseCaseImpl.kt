package com.kmstore.onlinestation.domain.usecases

import com.kmstore.onlinestation.data.BuildConfig.API_KEY
import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.data.repository.LocalSQLRepository
import com.kmstore.onlinestation.data.repository.TopStationRepository
import com.kmstore.onlinestation.data.entities.Constants.Companion.METHOD_GET_RADIOS
import com.kmstore.onlinestation.data.entities.Constants.Companion.RADIOS_LIMIT
import com.kmstore.onlinestation.data.entities.Constants.Companion.RADIOS_OFFSET
import com.kmstore.onlinestation.data.entities.RadioException
import com.kmstore.onlinestation.domain.interactors.TopStationInteractorUseCase
import com.kmstore.onlinestation.data.entities.request.QueryTopStationBody
import com.kmstore.onlinestation.domain.entities.StationItem
import com.kmstore.onlinestation.domain.utils.toDomain

internal class TopStationUseCaseImpl(
    private val topStationRepository: TopStationRepository,
    private val localSQLRepository: LocalSQLRepository
) : TopStationInteractorUseCase {

    private var offset: Int = -1
    private var stations = listOf<StationItem>()

    override suspend operator fun invoke(
        update: Boolean,
        value: List<StationItem>?
    ): DataResult<Pair<List<StationItem>, Boolean>> {

        offset = if (offset != -1) {
            if (!update) {
                offset.plus(RADIOS_OFFSET)
            } else {
                stations = stations.toMutableList().apply {
                    clear()
                }
                0
            }
        } else {
            stations = stations.toMutableList().apply {
                clear()
            }
            0
        }

        return when (val result = topStationRepository.getTopStations(
            QueryTopStationBody(
                METHOD_GET_RADIOS, API_KEY, offset,
                RADIOS_LIMIT, 1
            )
        )) {
            is DataResult.Success -> {
                val updatedItems = (result.data?.map { it.toDomain(false) } ?: listOf())
                    .map { localItem ->
                        val dbItem = localSQLRepository.getItemStationDB(localItem.id)
                        if (dbItem != null) {
                            localItem.copy(isFavorite = true)
                        } else {
                            localItem
                        }
                    }
                stations = stations.toMutableList().apply {
                    addAll(updatedItems)
                }
                val lastPage = updatedItems.size<RADIOS_OFFSET
                if (lastPage) offset = -1
                DataResult.Success(Pair(stations, lastPage))
            }

            is DataResult.Error -> {
                DataResult.Error(RadioException(result.errors.errorCode))
            }
        }
    }
}