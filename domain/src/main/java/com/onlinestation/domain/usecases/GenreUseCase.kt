package com.onlinestation.domain.usecases

import com.kmworks.appbase.utils.Constants.Companion.API_KEY
import com.kmworks.appbase.utils.Constants.Companion.METHOD_GET_GENRES
import com.kmworks.appbase.utils.Constants.Companion.errorDataNull
import com.kmworks.appbase.utils.Constants.Companion.errorDefaultCode
import com.onlinestation.data.datastore.GenreRepository

import com.onlinestation.domain.interactors.GenreInteractor
import com.onlinestation.domain.utils.fromGenreItemDbToGenreItem
import com.onlinestation.domain.utils.toGenreItem
import com.onlinestation.domain.utils.toGenreItemDb
import com.onlinestation.entities.RadioException
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.GenderItem
import com.onlinestation.entities.localmodels.QueryGenreBody

class GenreUseCase(private val genreRepository: GenreRepository) :
    GenreInteractor {

    override suspend fun getGenreListData(): Result<List<GenderItem>> {
        val data = genreRepository.getGenreListData(
            QueryGenreBody(
                API_KEY,
                METHOD_GET_GENRES
            )
        )
        return when (data) {
            is Result.Success -> {
                data.data?.let {
                    val mappingListForDB = it.map { item ->
                        item.toGenreItemDb()
                    }
                    genreRepository.saveGenreDB(mappingListForDB)
                    val mappingListForUI = it.map { item ->
                        item.toGenreItem()
                    }
                    Result.Success(mappingListForUI)
                } ?: Result.Error(RadioException(errorDefaultCode))
            }
            is Result.Error -> {
                Result.Error(RadioException(errorDefaultCode))
            }
        }
    }

    override fun getGenreListDataDB(): Result<List<GenderItem>> {
        genreRepository.getPrimaryGenreDataDB()?.apply {
            val mappingListForUI = this.map {
                it.fromGenreItemDbToGenreItem()
            }
            return Result.Success(mappingListForUI)
        }
        return Result.Error(RadioException(errorDataNull))
    }
}