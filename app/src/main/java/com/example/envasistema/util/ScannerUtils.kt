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

fun parseCsvToJson(rawScan: String): JSONObject {
    val values = rawScan.split(",")
    return JSONObject().apply {
        put("manga-id", values.getOrElse(0) { "" })
        put("molde", values.getOrElse(1) { "" })
        put("maquina", values.getOrElse(2) { "" })
        put("n_op", values.getOrElse(3) { "" })
        put("turno", values.getOrElse(4) { "" })
        put("fecha_de_ot", values.getOrElse(5) { "" })
        put("n_ot_correlativo", values.getOrElse(6) { "0" }.toIntOrNull() ?: 0)
        put("operador", values.getOrElse(7) { "" })
        put("color", values.getOrElse(8) { "" })
        put("fecha_hora_pesaje", values.getOrElse(9) { "" })
        put("peso_final_kg", values.getOrElse(10) { "0.0" }.toDoubleOrNull() ?: 0.0)
    }
}

fun extractMangaIdFromScan(rawScannedString: String): String {
    return try {
        if (rawScannedString.contains(",")) {
            rawScannedString.split(",").getOrElse(0) { rawScannedString }
        } else {
            JSONObject(rawScannedString).getString("manga-id")
        }
    } catch (e: Exception) {
        rawScannedString 
    }
}

fun getCurrentTimestampIso(): String {
    return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
}
