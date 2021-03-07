package com.onlinestation.data.dataservice.sqlservice

import androidx.room.*
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemDb

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertStation(item: StationItemDb)

    @Query("DELETE  FROM stations where id=:itemId")
     fun deleteStationById(itemId: Long)

    @Query("SELECT * FROM stations")
     fun getAllStationList(): MutableList<StationItemDb>

    @Query("SELECT * FROM stations where id=:itemId")
     fun getItemStation(itemId: Long): StationItemDb?


}