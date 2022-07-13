package com.nextidea.onlinestation.domain.usecases

import com.nextidea.onlinestation.data.BuildConfig
import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.data.repository.GenreRepository
import com.nextidea.onlinestation.data.entities.Constants
import com.nextidea.onlinestation.data.entities.Constants.Companion.METHOD_GET_GENRES
import com.nextidea.onlinestation.data.entities.Constants.Companion.errorDataNull
import com.nextidea.onlinestation.data.entities.RadioException
import com.nextidea.onlinestation.data.entities.gendermodels.ResponseGender.Companion.toDomain
import com.nextidea.onlinestation.domain.interactors.GenreInteractorUseCase
import com.nextidea.onlinestation.data.entities.request.GenderItem
import com.nextidea.onlinestation.data.entities.request.QueryGenreBody
import com.nextidea.onlinestation.domain.utils.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
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
                result.data.let {
                    val mappingListForUI = it.map { item ->
                        item.toDomain()
                    }
                    genders = genders.toMutableList().apply {
                        addAll(mappingListForUI)
                    }
                    val lastPage = mappingListForUI.size < Constants.RADIOS_OFFSET
                    if (lastPage) offset = -1
                    DataResult.Success(Pair(genders, lastPage))
                }
            }
            is DataResult.Error -> {
                DataResult.Error(RadioException(result.errors.errorCode))
            }
        }
    }


}