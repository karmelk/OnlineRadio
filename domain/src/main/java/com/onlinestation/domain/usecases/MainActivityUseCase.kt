package com.onlinestation.domain.usecases

import com.kmworks.appbase.Constants
import com.onlinestation.data.datastore.PrimaryGenreRepository
import com.onlinestation.domain.interactors.MainActivityInteractor
import com.onlinestation.domain.utils.toGenreItem
import com.onlinestation.entities.RadioException
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.GenderItem


class MainActivityUseCase(private val primaryGenreRepository: PrimaryGenreRepository) :
    MainActivityInteractor {
    override fun getPrimaryGenreDB(): Result<MutableList<GenderItem>> {
        val genderItems: MutableList<GenderItem> = mutableListOf()
        primaryGenreRepository.getPrimaryGenreDataDB()?.apply {
            for (item in this) {
                genderItems.add(item.toGenreItem())
            }
            if(genderItems.isNotEmpty()){
                genderItems.add(0,GenderItem("All",true))
            }
            return Result.Success(genderItems)
        }
        return Result.Error(RadioException(Constants.errorDataNull))
    }
}