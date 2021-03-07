package com.onlinestation.domain.usecases

import com.kmworks.appbase.utils.Constants
import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.data.datastore.GenreRepository
import com.onlinestation.domain.interactors.MainActivityInteractor
import com.onlinestation.entities.RadioException
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.GenderItem
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import kotlinx.coroutines.*


class MainActivityUseCase(
    private val genreRepository: GenreRepository,
    private val localSQLRepository: LocalSQLRepository
) :
    MainActivityInteractor {
    override fun getGenderDB(): Result<MutableList<GenderItem>> {
        val genderItems: MutableList<GenderItem> = mutableListOf()
        genreRepository.getPrimaryGenreDataDB()?.apply {
            /*   for (item in this) {
                   genderItems.add(item.toGenreItem())
               }*/
            /*  if (genderItems.isNotEmpty()) {
                  genderItems.add(0, GenderItem("All", true))
              }*/
            return Result.Success(genderItems)
        }
        return Result.Error(RadioException(Constants.errorDataNull))
    }

    override suspend fun checkStationInDB(itemId: Int): Boolean {
     /*   genreRepository.checkStationInDB(itemId)?.let {
            return true
        } ?: */return false
    }

    override fun getBalanceData() {
        val balanceData = localSQLRepository.getBalanceDB()
        if (balanceData == null) {
            CoroutineScope(Dispatchers.IO).launch {
                localSQLRepository.rewardBalanceDB(
                    OwnerUserBalance(
                        Constants.defaultUserID,
                        Constants.defaultUserBalanceCount
                    )
                )
            }
        }
    }


}