package com.onlinestation.domain.usecases

import com.onlinestation.data.datastore.SecondaryGenreRepository
import com.kmworks.appbase.Constants.Companion.API_KEY
import com.kmworks.appbase.Constants.Companion.DATA_FORMAT

import com.onlinestation.domain.interactors.SecondaryGenreInteractor
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QuerySecondaryGenreBody
import com.onlinestation.entities.responcemodels.gendermodels.SecondaryGenreItem

class SecondaryGenreUseCase(private val secondaryGenreRepository: SecondaryGenreRepository) :
    SecondaryGenreInteractor {
    override suspend fun getSecondaryGenreListData(parentId: Int): Result<MutableList<SecondaryGenreItem>> =
        secondaryGenreRepository.getSecondaryGenreData(QuerySecondaryGenreBody(parentId, API_KEY, DATA_FORMAT))


}