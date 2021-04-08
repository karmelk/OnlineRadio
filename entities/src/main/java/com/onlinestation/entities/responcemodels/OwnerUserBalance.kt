package com.onlinestation.entities.responcemodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ownerBalance")
data class OwnerUserBalance(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "balance")
    val balance: Int = 0
)