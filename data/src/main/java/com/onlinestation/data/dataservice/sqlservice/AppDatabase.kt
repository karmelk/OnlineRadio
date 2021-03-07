package com.onlinestation.data.dataservice.sqlservice

import androidx.room.Database
import androidx.room.RoomDatabase
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.gendermodels.server.GenderItemDb
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemDb

@Database(
    entities = [OwnerUserBalance::class, GenderItemDb::class,StationItemDb::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun balanceDao(): BalanceDto
    abstract fun genreDao(): GenreDto
}