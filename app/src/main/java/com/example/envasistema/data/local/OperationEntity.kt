package com.example.envasistema.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "operations",
    indices = [Index(value = ["codigo_qr", "tipo_operacion"], unique = true)]
)
data class OperationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigo_qr: String,
    val tipo_operacion: String,
    val locacion_origen: String,
    val locacion_destino: String,
    val operario_id: String,
    val metadatos: String,
    val timestamp: String,
    val isSynced: Boolean = false
)
