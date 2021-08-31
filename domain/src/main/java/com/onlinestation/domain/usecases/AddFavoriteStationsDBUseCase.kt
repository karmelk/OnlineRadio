package com.onlinestation.domain.usecases


import com.onlinestation.data.datastore.FavoriteStationsRepository
import com.onlinestation.data.entities.Constants.Companion.notFountItemIntoListExaction
import com.onlinestation.data.entities.RadioException
import com.onlinestation.domain.interactors.AddFavoriteStationsDBInteractor
import com.onlinestation.domain.utils.fromStationToStationDB
import com.onlinestation.domain.utils.toDomain
import com.onlinestation.domain.entities.StationItem
import com.onlinestation.data.entities.Result

class AddFavoriteStationsDBUseCase(private val favoriteStationsRepository: FavoriteStationsRepository) :
    AddFavoriteStationsDBInteractor {

    private var stationItems = listOf<StationItem>()

    override suspend fun addRemoveStationFromDB(item: StationItem): Result<Pair<List<StationItem>, StationItem>> {
        if (item.isFavorite) {
            val removeItem = stationItems.find { it.id == item.id }?: return Result.Error(
                RadioException(notFountItemIntoListExaction)
            )
            favoriteStationsRepository.removeStationLocalDB(item.id)
            val updateItem=removeItem.copy(isFavorite = false)
            stationItems = stationItems.toMutableList().apply {
                this.remove(removeItem)
            }
            return Result.Success(Pair(stationItems, updateItem))
        } else {
            val itemLocalDb = item.fromStationToStationDB()
            val updateItem = item.copy(isFavorite = true)
            stationItems = stationItems.toMutableList().apply {
                add(updateItem)
            }
            favoriteStationsRepository.addStationLocalDB(itemLocalDb)

            return Result.Success(Pair(stationItems,updateItem))
        }

    }

    override suspend fun getAllStationDataLocalDB(): List<StationItem> {
        stationItems = favoriteStationsRepository.getAllStationListLocalDB().map {
            it.toDomain(true)
        }.toMutableList()

        return stationItems
    }

}