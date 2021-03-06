package com.onlinestation.data.dataservice.sqlservice

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.onlinestation.entities.responcemodels.OwnerUserBalance

@Dao
interface BalanceDto {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateBalance(item: OwnerUserBalance)

    @Query("DELETE FROM ownerBalance")
    fun clearBalance()

    @Query("SELECT * FROM ownerBalance")
    fun getBalance(): OwnerUserBalance?

}