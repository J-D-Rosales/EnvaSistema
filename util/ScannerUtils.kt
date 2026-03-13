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
    val isSynced: Boolean = false,
    
    // New Fields for Structured Data
    val extra1: String? = null,
    val extra2: String? = null,
    val extra3: String? = null,
    val pieza_nombre: String = "Producto",
    val peso_kg: Double = 0.0
)

/**
 * Parses the structured QR CSV string based on the new format:
 * 0: pesaje.id (manga-id)
 * 1: molde
 * 2: maquina
 * 3: nro_op
 * 4: turno
 * 5: fecha_ot_str
 * 6: nro_orden_trabajo (extra1)
 * 7: operador (extra2)
 * 8: color (extra3)
 * 9: fecha_hora_str
 * 10: peso_kg
 * 11: pieza_sku
 * 12: pieza_nombre
 */
fun parseCsvToJson(rawScan: String): JSONObject {
    val values = rawScan.split(";")
    return JSONObject().apply {
        put("manga-id", values.getOrElse(0) { "" })
        put("molde", values.getOrElse(1) { "" })
        put("maquina", values.getOrElse(2) { "" })
        put("nro_op", values.getOrElse(3) { "" })
        put("turno", values.getOrElse(4) { "" })
        put("fecha_ot_str", values.getOrElse(5) { "" })
        put("nro_orden_trabajo", values.getOrElse(6) { "" })
        put("operador", values.getOrElse(7) { "" })
        put("color", values.getOrElse(8) { "" })
        put("fecha_hora_str", values.getOrElse(9) { "" })
        put("peso_final_kg", values.getOrElse(10) { "0.0" }.toDoubleOrNull() ?: 0.0)
        put("pieza_sku", values.getOrElse(11) { "" })
        put("pieza_nombre", values.getOrElse(12) { "Producto" })
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
