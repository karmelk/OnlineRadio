package com.onlinestation.data.dataservice.sqlservice

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.gendermodels.PrimaryGenreItem

@Dao
interface GenreDto {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGenre(data: MutableList<PrimaryGenreItem>)

    @Query("DELETE FROM primaryGenre")
    fun clearGenre()

    @Query("SELECT * FROM primaryGenre")
    fun getGenreList(): MutableList<PrimaryGenreItem>?
}