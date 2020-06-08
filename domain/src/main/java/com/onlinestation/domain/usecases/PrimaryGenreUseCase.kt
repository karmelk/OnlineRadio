package com.onlinestation.domain.usecases

import com.kmworks.appbase.Constants
import com.onlinestation.data.datastore.PrimaryGenreRepository
import com.kmworks.appbase.Constants.Companion.API_KEY
import com.kmworks.appbase.Constants.Companion.DATA_FORMAT
import com.kmworks.appbase.Constants.Companion.errorDataNull
import com.kmworks.appbase.Constants.Companion.errorDefaultCode
import com.onlinestation.domain.interactors.PrimaryGenreInteractor
import com.onlinestation.entities.RadioException
import com.onlinestation.entities.responcemodels.gendermodels.PrimaryGenreItem
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryPrimaryGenreBody

class PrimaryGenreUseCase(private val primaryGenreRepository: PrimaryGenreRepository) :
    PrimaryGenreInteractor {

    override suspend fun getPrimaryGenreListData(): Result<MutableList<PrimaryGenreItem>> {
        val genreData = primaryGenreRepository.getPrimaryGenreData(
            QueryPrimaryGenreBody(
                API_KEY,
                DATA_FORMAT
            )
        )
        return when (genreData) {
            is Result.Success -> {
                genreData.data?.let {

                    primaryGenreRepository.saveGenreDB(it)
                    Result.Success(it)
                } ?: Result.Error(RadioException(errorDataNull))
            }
            is Result.Error -> {
                Result.Error(RadioException(errorDefaultCode))
            }
        }
    }

    override fun getPrimaryGenreListDataDB(): Result<MutableList<PrimaryGenreItem>> {
        primaryGenreRepository.getPrimaryGenreDataDB()?.apply {
            return Result.Success(this)
        }
        return Result.Error(RadioException(errorDataNull))
    }
}