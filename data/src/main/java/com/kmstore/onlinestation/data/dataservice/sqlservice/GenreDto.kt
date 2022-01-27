package com.kmstore.onlinestation.data.dataservice.sqlservice

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kmstore.onlinestation.data.entities.gendermodels.GenderItemDb

@Dao
interface GenreDto {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGenre(data: List<GenderItemDb>)

    @Query("DELETE FROM genre")
    fun clearGenre()

    @Query("SELECT * FROM genre")
    fun getGenreList(): List<GenderItemDb>?
}