package com.example.envasistema.data.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.envasistema.data.local.AppDatabase
import com.example.envasistema.data.local.OperationEntity
import com.example.envasistema.data.remote.toNetworkDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val database = AppDatabase.getDatabase(applicationContext)
        val dao = database.operationDao()

        val pendingOperations = dao.getAllPendingOperations()
        
        if (pendingOperations.isEmpty()) {
            return@withContext Result.success()
        }

        Log.d("SyncWorker", "Starting sync for ${pendingOperations.size} operations")

        var allSuccessful = true
        for (operation in pendingOperations) {
            // Map to DTO before sending to the cloud
            val success = uploadToTheCloud(operation)
            if (success) {
                dao.deleteOperation(operation.id)
                Log.d("SyncWorker", "🗑️ DELETED LOCALLY: ${operation.codigo_qr}")
            } else {
                allSuccessful = false
                Log.e("SyncWorker", "Failed to sync operation ID: ${operation.id}")
            }
        }

        if (allSuccessful) Result.success() else Result.retry()
    }

    private suspend fun uploadToTheCloud(operation: OperationEntity): Boolean {
        // Convert to DTO to exclude 'peso_kg' from serialization
        val networkPayload = operation.toNetworkDto()
        
        Log.d("SyncWorker", "NETWORK UPLOAD: Sending DTO (excluding peso_kg) -> $networkPayload")
        
        // Simulate network delay
        kotlinx.coroutines.delay(1000)
        
        // Return false as per original requirements to simulate pending status
        return false 
    }
}
