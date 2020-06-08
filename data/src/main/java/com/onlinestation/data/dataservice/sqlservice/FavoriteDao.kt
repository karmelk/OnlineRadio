package com.onlinestation.data.dataservice.sqlservice

import androidx.room.*
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertStation(item: StationItemLocal)

    @Query("DELETE  FROM radioStation where id=:itemId")
     fun deleteStationById(itemId: Int)

    @Query("SELECT * FROM radioStation order by createDateTime DESC ")
     fun getAllStationList(): MutableList<StationItemLocal>

    @Query("SELECT * FROM radioStation where id=:itemId")
     fun getItemStation(itemId: Int): StationItemLocal?


}