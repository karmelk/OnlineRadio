package com.kmstore.onlinestation.domain.usecases

import com.kmstore.onlinestation.data.BuildConfig
import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.data.repository.GenreRepository
import com.kmstore.onlinestation.data.entities.Constants
import com.kmstore.onlinestation.data.entities.Constants.Companion.METHOD_GET_GENRES
import com.kmstore.onlinestation.data.entities.Constants.Companion.errorDataNull
import com.kmstore.onlinestation.data.entities.RadioException
import com.kmstore.onlinestation.domain.interactors.GenreInteractorUseCase
import com.kmstore.onlinestation.data.entities.request.GenderItem
import com.kmstore.onlinestation.data.entities.request.QueryGenreBody
import com.kmstore.onlinestation.domain.utils.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class GenreUseCaseImpl(private val genreRepository: GenreRepository) :
    GenreInteractorUseCase {

    private var offset: Int = -1
    private var genders = listOf<GenderItem>()

    override suspend operator fun invoke(
        update: Boolean,
        value: List<GenderItem>?
    ): DataResult<Pair<List<GenderItem>, Boolean>> = withContext(Dispatchers.IO) {

        offset = if (offset != -1) {
            if (!update) {
                offset.plus(Constants.RADIOS_OFFSET)
            } else {
                genders = genders.toMutableList().apply {
                    clear()
                }
                0
            }
        } else {
            genders = genders.toMutableList().apply {
                clear()
            }
            0
        }

        val result = genreRepository.getGenreListData(
            QueryGenreBody(
                BuildConfig.API_KEY,
                METHOD_GET_GENRES
            )
        )

        when (result) {
            is DataResult.Success -> {
                result.data?.let {
                    val mappingListForUI = it.map { item ->
                        item.toDomain()
                    }
                    genders = genders.toMutableList().apply {
                        addAll(mappingListForUI)
                    }
                    val lastPage = mappingListForUI.size < Constants.RADIOS_OFFSET
                    if (lastPage) offset = -1
                    DataResult.Success(Pair(genders, lastPage))
                } ?: DataResult.Error(RadioException(errorDataNull))
            }
            is DataResult.Error -> {
                DataResult.Error(RadioException(result.errors.errorCode))
            }
        }
    }


}