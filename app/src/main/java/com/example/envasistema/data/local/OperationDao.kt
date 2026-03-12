package com.example.envasistema.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OperationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(operation: OperationEntity)

    @Query("SELECT * FROM operations")
    suspend fun getAllPendingOperations(): List<OperationEntity>

    @Query("DELETE FROM operations WHERE id = :operationId")
    suspend fun deleteOperation(operationId: Int)
}
