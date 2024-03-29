package com.nextidea.onlinestation.domain.usecases


import com.nextidea.onlinestation.data.datastore.FavoriteStationsRepository
import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.data.entities.Constants.Companion.notFountItemIntoListExaction
import com.nextidea.onlinestation.data.entities.RadioException
import com.nextidea.onlinestation.domain.interactors.AddFavoriteStationsDBUseCase
import com.nextidea.onlinestation.domain.utils.fromStationToStationDB
import com.nextidea.onlinestation.domain.entities.StationItem
import com.nextidea.onlinestation.domain.utils.toDomain

internal class GetAddFavoriteStationsDBUseCaseImpl(private val favoriteStationsRepository: FavoriteStationsRepository) :
    AddFavoriteStationsDBUseCase {

    private var stationItems = listOf<StationItem>()

    override suspend fun addRemoveStationFromDB(item: StationItem): DataResult<Pair<List<StationItem>, StationItem>> {
        if (item.isFavorite) {
            val removeItem = stationItems.find { it.id == item.id }?: return DataResult.Error(
                RadioException(notFountItemIntoListExaction)
            )
            favoriteStationsRepository.removeStationLocalDB(item.id)
            val updateItem=removeItem.copy(isFavorite = false)
            stationItems = stationItems.toMutableList().apply {
                this.remove(removeItem)
            }
            return DataResult.Success(Pair(stationItems, updateItem))
        } else {
            val itemLocalDb = item.fromStationToStationDB()
            val updateItem = item.copy(isFavorite = true)
            stationItems = stationItems.toMutableList().apply {
                add(updateItem)
            }
            favoriteStationsRepository.addStationLocalDB(itemLocalDb)

            return DataResult.Success(Pair(stationItems,updateItem))
        }

    }

    override suspend fun getAllStationDataLocalDB(): List<StationItem> {
        stationItems = favoriteStationsRepository.getAllStationListLocalDB().map {
            it.toDomain(true)
        }.toMutableList()

        return stationItems
    }

}