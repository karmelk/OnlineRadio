package com.onlinestation.data.dataservice.sqlservice

import androidx.room.*
import com.onlinestation.data.entities.stationmodels.StationItemDb

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertStation(item: StationItemDb)

    @Query("DELETE  FROM stations where id=:itemId")
     fun deleteStationById(itemId: Int)

    @Query("SELECT * FROM stations")
     fun getAllStationList(): List<StationItemDb>

    @Query("SELECT * FROM stations where id=:itemId")
     fun getItemStation(itemId: Int): StationItemDb?


}