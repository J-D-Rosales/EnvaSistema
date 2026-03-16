package com.example.envasistema.data.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.envasistema.data.local.AppDatabase
import com.example.envasistema.data.local.OperationEntity
import com.example.envasistema.data.remote.RetrofitClient
import com.example.envasistema.data.remote.toNetworkMovementDto
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
        return try {
            val networkPayload = operation.toNetworkMovementDto()
            val response = RetrofitClient.kardexApi.registerMovement(networkPayload)
            
            if (response.isSuccessful) {
                Log.d("SyncWorker", "✅ SYNC SUCCESS: ${operation.codigo_qr}")
                true
            } else {
                Log.e("SyncWorker", "❌ SYNC ERROR: ${response.code()} - ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "⚠️ NETWORK EXCEPTION: ${e.message}")
            false
        }
    }
}
