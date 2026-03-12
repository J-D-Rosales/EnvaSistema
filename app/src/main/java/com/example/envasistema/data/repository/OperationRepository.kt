package com.example.envasistema.data.repository

import android.content.Context
import androidx.work.*
import com.example.envasistema.data.local.AppDatabase
import com.example.envasistema.data.local.OperationEntity
import com.example.envasistema.data.sync.SyncWorker
import com.example.envasistema.util.OperationPayload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OperationRepository(private val context: Context) {

    private val database = AppDatabase.getDatabase(context)
    private val operationDao = database.operationDao()

    /**
     * Saves an OperationPayload to the local Room database and triggers a background sync.
     */
    suspend fun saveOperation(payload: OperationPayload) = withContext(Dispatchers.IO) {
        // 1. Map Payload to Entity
        val entity = OperationEntity(
            codigo_qr = payload.codigo_qr,
            tipo_operacion = payload.tipo_operacion,
            locacion_origen = payload.locacion_origen,
            locacion_destino = payload.locacion_destino,
            operario_id = payload.operario_id,
            metadatos = payload.metadatos,
            timestamp = payload.timestamp,
            isSynced = false
        )

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
