package com.example.envasistema.util

import org.json.JSONObject
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class OperationPayload(
    val codigo_qr: String,
    val tipo_operacion: String,
    val locacion_origen: String,
    val locacion_destino: String,
    val operario_id: String,
    val metadatos: String,
    val timestamp: String,
    val isSynced: Boolean = false
)

fun extractMangaIdFromScan(rawScannedString: String): String {
    return try {
        JSONObject(rawScannedString).getString("manga-id")
    } catch (e: Exception) {
        rawScannedString 
    }
}

fun getCurrentTimestampIso(): String {
    return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
}
