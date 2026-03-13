package com.example.envasistema.data.remote

import com.example.envasistema.data.local.OperationEntity

/**
 * Data Transfer Object (DTO) for network requests.
 * This class excludes fields that the backend does not require, such as 'peso_kg'.
 */
data class NetworkOperationDto(
    val codigo_qr: String,
    val tipo_operacion: String,
    val locacion_origen: String,
    val locacion_destino: String,
    val operario_id: String,
    val metadatos: String,
    val timestamp: String,
    val extra1: String? = null,
    val extra2: String? = null,
    val extra3: String? = null,
    val pieza_nombre: String
)

/**
 * Mapper extension function to convert a local Room Entity to a Network DTO.
 */
fun OperationEntity.toNetworkDto(): NetworkOperationDto {
    return NetworkOperationDto(
        codigo_qr = this.codigo_qr,
        tipo_operacion = this.tipo_operacion,
        locacion_origen = this.locacion_origen,
        locacion_destino = this.locacion_destino,
        operario_id = this.operario_id,
        metadatos = this.metadatos,
        timestamp = this.timestamp,
        extra1 = this.extra1,
        extra2 = this.extra2,
        extra3 = this.extra3,
        pieza_nombre = this.pieza_nombre
    )
}
