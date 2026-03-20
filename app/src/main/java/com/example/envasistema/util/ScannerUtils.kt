package com.example.envasistema.util

import com.example.envasistema.data.local.OperationEntity
import org.json.JSONObject
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import com.amplifyframework.core.Amplify
import com.amplifyframework.auth.AuthUserAttributeKey
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Data class representing the operation to be performed.
 */
data class OperationPayload(
    val codigo_qr: String,
    val tipo_operacion: String,
    val locacion_origen: String,
    val locacion_destino: String,
    val operario_id: String,
    val metadatos: String,
    val timestamp: String,
    val isSynced: Boolean = false,
    val extra1: String? = null,
    val extra2: String? = null,
    val extra3: String? = null,
    val pieza_nombre: String = "Producto",
    val peso_kg: Double = 0.0,
    var creator_email: String? = null,
    var creator_name: String? = null
)

/**
 * Mapper to convert OperationPayload to OperationEntity.
 */
fun OperationPayload.toEntity(): OperationEntity {
    return OperationEntity(
        codigo_qr = this.codigo_qr,
        tipo_operacion = this.tipo_operacion,
        locacion_origen = this.locacion_origen,
        locacion_destino = this.locacion_destino,
        operario_id = this.operario_id,
        metadatos = this.metadatos,
        timestamp = this.timestamp,
        isSynced = this.isSynced,
        extra1 = this.extra1,
        extra2 = this.extra2,
        extra3 = this.extra3,
        pieza_nombre = this.pieza_nombre,
        peso_kg = this.peso_kg,
        creator_email = this.creator_email,
        creator_name = this.creator_name
    )
}

suspend fun OperationPayload.injectAuthData(): OperationPayload {
    return suspendCancellableCoroutine { continuation ->
        Amplify.Auth.fetchUserAttributes(
            { attributes ->
                this.creator_email = attributes.find { it.key == AuthUserAttributeKey.email() }?.value
                this.creator_name = attributes.find { it.key == AuthUserAttributeKey.name() }?.value
                continuation.resume(this)
            },
            { error ->
                continuation.resume(this)
            }
        )
    }
}

/**
 * Robustly parses the structured QR string based on the semicolon (;) format.
 */
fun parseQrToPayload(
    rawScan: String,
    tipoOperacion: String,
    locacionOrigen: String,
    locacionDestino: String,
    operarioId: String
): OperationPayload? {
    val values = rawScan.split(";").map { it.trim() }

    if (values.size < 13) return null

    return try {
        val mangaId = values[0]
        val pesoKg = values[10].toDoubleOrNull() ?: 0.0
        val piezaNombre = values.getOrElse(12) { "Producto" }
        
        val extra1 = values.getOrNull(13)
        val extra2 = values.getOrNull(14)
        val extra3 = values.getOrNull(15)

        val metadatosJson = JSONObject().apply {
            put("molde", values.getOrElse(1) { "" })
            put("maquina", values.getOrElse(2) { "" })
            put("nro_op", values.getOrElse(3) { "" })
            put("turno", values.getOrElse(4) { "" })
            put("fecha_ot_str", values.getOrElse(5) { "" })
            put("nro_orden_trabajo", values.getOrElse(6) { "" })
            put("operador", values.getOrElse(7) { "" })
            put("color", values.getOrElse(8) { "" })
            put("fecha_hora_str", values.getOrElse(9) { "" })
        }.toString()

        OperationPayload(
            codigo_qr = mangaId,
            tipo_operacion = tipoOperacion,
            locacion_origen = locacionOrigen,
            locacion_destino = locacionDestino,
            operario_id = operarioId,
            metadatos = metadatosJson,
            timestamp = getCurrentTimestampIso(),
            pieza_nombre = if (piezaNombre.isBlank()) "Producto" else piezaNombre,
            peso_kg = pesoKg,
            extra1 = extra1,
            extra2 = extra2,
            extra3 = extra3
        )
    } catch (e: Exception) {
        null
    }
}

/**
 * Parses the structured QR CSV string into a JSONObject.
 */
fun parseCsvToJson(rawScan: String): JSONObject {
    val values = rawScan.split(";").map { it.trim() }
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
        val delimiter = if (rawScannedString.contains(";")) ";" else ","
        if (rawScannedString.contains(delimiter)) {
            rawScannedString.split(delimiter).getOrElse(0) { rawScannedString }.trim()
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
