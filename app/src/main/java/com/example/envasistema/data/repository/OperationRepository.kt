package com.example.envasistema.data.repository

import android.content.Context
import androidx.work.*
import com.example.envasistema.data.local.AppDatabase
import com.example.envasistema.data.sync.SyncWorker
import com.example.envasistema.util.OperationPayload
import com.example.envasistema.util.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OperationRepository(private val context: Context) {

    private val database = AppDatabase.getDatabase(context)
    private val operationDao = database.operationDao()

    /**
     * Saves an OperationPayload to the local Room database and triggers a background sync.
     */
    suspend fun saveOperation(payload: OperationPayload) = withContext(Dispatchers.IO) {
        // 1. Map Payload to Entity using the new extension function
        // This ensures piezo_nombre, peso_kg, extra1, extra2, and extra3 are preserved.
        val entity = payload.toEntity()

        // 2. Save to Room
        operationDao.insert(entity)

        // 3. Enqueue WorkManager task for background sync
        enqueueSyncWork()
    }

    private fun enqueueSyncWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                java.util.concurrent.TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "OperationSyncWork",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }
}
