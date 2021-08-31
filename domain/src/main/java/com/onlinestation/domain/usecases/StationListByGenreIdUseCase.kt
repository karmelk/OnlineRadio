package com.onlinestation.domain.usecases


import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.data.datastore.StationListByGenreIdRepository
import com.onlinestation.data.entities.Constants.Companion.API_KEY
import com.onlinestation.data.entities.Constants.Companion.METHOD_GET_RADIOS
import com.onlinestation.data.entities.Constants.Companion.RADIOS_LIMIT
import com.onlinestation.data.entities.Constants.Companion.RADIOS_OFFSET
import com.onlinestation.data.entities.Constants.Companion.errorDefaultCode
import com.onlinestation.data.entities.RadioException
import com.onlinestation.domain.interactors.StationListByGenreIdInteractor
import com.onlinestation.data.entities.request.QueryStationByGenderBody
import com.onlinestation.data.entities.Result
import com.onlinestation.domain.entities.StationItem
import com.onlinestation.domain.utils.toDomain

class StationListByGenreIdUseCase(
    private val stationListByGenreIdRepository: StationListByGenreIdRepository,
    private val localSQLRepository: LocalSQLRepository
) : StationListByGenreIdInteractor {

    override suspend operator fun invoke(id: Long): Result<List<StationItem>> =
        when (val apiList = stationListByGenreIdRepository.getStationListData(
            QueryStationByGenderBody(
                METHOD_GET_RADIOS, API_KEY, RADIOS_OFFSET,
                RADIOS_LIMIT, id
            )
        )) {
            is Result.Success -> {
                val updatedItems = (apiList.data?.map { it.toDomain(false) }
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
            else -> {
                Result.Error(RadioException(errorDefaultCode))
            }
        }

}