package com.onlinestation.data.dataservice.sqlservice

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.onlinestation.entities.responcemodels.gendermodels.server.GenderItemDb

@Dao
interface GenreDto {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGenre(data: List<GenderItemDb>)

    @Query("DELETE FROM genre")
    fun clearGenre()

    @Query("SELECT * FROM genre")
    fun getGenreList(): MutableList<GenderItemDb>?
}