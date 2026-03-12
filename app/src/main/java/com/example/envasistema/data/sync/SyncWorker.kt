package com.example.envasistema.data.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.envasistema.data.local.AppDatabase
import com.example.envasistema.data.local.OperationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val database = AppDatabase.getDatabase(applicationContext)
        val dao = database.operationDao()

        val unsyncedOperations = dao.getUnsyncedOperations()
        
        if (unsyncedOperations.isEmpty()) {
            return@withContext Result.success()
        }

        Log.d("SyncWorker", "Starting sync for ${unsyncedOperations.size} operations")

        var allSuccessful = true
        for (operation in unsyncedOperations) {
            val success = uploadToTheCloud(operation)
            if (success) {
                dao.updateSyncStatus(operation.id, true)
                Log.d("SyncWorker", "Successfully synced operation ID: ${operation.id}")
            } else {
                allSuccessful = false
                Log.e("SyncWorker", "Failed to sync operation ID: ${operation.id}")
            }
        }

        if (allSuccessful) Result.success() else Result.retry()
    }

    /**
     * Stub function for cloud upload as requested.
     * Currently returns false to keep data in local Room database as pending.
     */
    private suspend fun uploadToTheCloud(payload: OperationEntity): Boolean {
        Log.d("SyncWorker", "FAKE UPLOAD: Sending payload to cloud -> ${payload.codigo_qr}")
        // Simulate network delay
        kotlinx.coroutines.delay(1000)
        // Returns false as per requirements to simulate pending sync
        return false 
    }
}
