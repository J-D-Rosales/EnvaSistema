package com.example.envasistema.data.remote

import com.example.envasistema.data.local.OperationEntity
import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) for synchronization with the backend.
 * The 'peso_kg' field is intentionally omitted as per backend requirements.
 */
data class NetworkMovementDto(
    @SerializedName("codigo_qr") val codigoQr: String,
    @SerializedName("tipo_operacion") val tipoOperacion: String,
    @SerializedName("locacion_origen") val locacionOrigen: String,
    @SerializedName("locacion_destino") val locacionDestino: String,
    @SerializedName("operario_id") val operarioId: String,
    @SerializedName("metadatos") val metadatos: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("pieza_nombre") val piezaNombre: String,
    @SerializedName("extra1") val extra1: String? = null,
    @SerializedName("extra2") val extra2: String? = null,
    @SerializedName("extra3") val extra3: String? = null
)

/**
 * Mapper extension function to convert a local Room Entity to a Network DTO.
 */
fun OperationEntity.toNetworkMovementDto(): NetworkMovementDto {
    return NetworkMovementDto(
        codigoQr = this.codigo_qr,
        tipoOperacion = this.tipo_operacion,
        locacionOrigen = this.locacion_origen,
        locacionDestino = this.locacion_destino,
        operarioId = this.operario_id,
        metadatos = this.metadatos,
        timestamp = this.timestamp,
        piezaNombre = this.pieza_nombre,
        extra1 = this.extra1,
        extra2 = this.extra2,
        extra3 = this.extra3
    )
}
