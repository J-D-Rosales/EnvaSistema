package com.example.envasistema.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OperationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(operation: OperationEntity)

    @Query("SELECT * FROM operations WHERE isSynced = 0")
    suspend fun getUnsyncedOperations(): List<OperationEntity>

    @Query("UPDATE operations SET isSynced = :isSynced WHERE id = :id")
    suspend fun updateSyncStatus(id: Int, isSynced: Boolean)
}
