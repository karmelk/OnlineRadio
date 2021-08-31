package com.onlinestation.domain.usecases

import com.onlinestation.data.datastore.GenreRepository
import com.onlinestation.data.entities.Constants.Companion.API_KEY
import com.onlinestation.data.entities.Constants.Companion.METHOD_GET_GENRES
import com.onlinestation.data.entities.Constants.Companion.errorDataNull
import com.onlinestation.data.entities.RadioException
import com.onlinestation.domain.interactors.GenreInteractor
import com.onlinestation.domain.utils.toDomain
import com.onlinestation.data.entities.request.GenderItem
import com.onlinestation.data.entities.request.QueryGenreBody
import com.onlinestation.data.entities.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GenreUseCase(private val genreRepository: GenreRepository) :
    GenreInteractor {

    override suspend operator fun invoke(): Result<List<GenderItem>> = withContext(Dispatchers.IO) {
        val result = genreRepository.getGenreListData(
            QueryGenreBody(
                API_KEY,
                METHOD_GET_GENRES
            )
        )

        when (result) {
            is Result.Success -> {
                result.data?.let {
                    val mappingListForUI = it.map { item ->
                        item.toDomain()
                    }
                    Result.Success(mappingListForUI)
                } ?: Result.Error(RadioException(errorDataNull))
            }
            is Result.Error -> {
                Result.Error(RadioException(result.errors.errorCode))
            }
        }
    }
}