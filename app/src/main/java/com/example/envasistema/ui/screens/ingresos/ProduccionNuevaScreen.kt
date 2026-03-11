package com.example.envasistema.ui.screens.ingresos

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.envasistema.ui.components.ScanningLayout
import com.example.envasistema.util.OperationPayload
import com.example.envasistema.util.getCurrentTimestampIso
import org.json.JSONObject

@Composable
fun ProduccionNuevaScreen(onBackClick: () -> Unit) {
    ScanningLayout(
        title = "Producción Nueva",
        subtitle = "INGRESOS",
        infoText = "Presione botón lateral para escanear código QR de la pieza / manga",
        onBackClick = onBackClick,
        onSaveClick = { scannedCodes ->
            val payloadsToSave = mutableListOf<OperationPayload>()
            val currentTimestamp = getCurrentTimestampIso()

            scannedCodes.forEach { rawScan ->
                try {
                    val qrJson = JSONObject(rawScan)
                    val mangaId = qrJson.optString("manga-id", rawScan)
                    
                    val metadatosJson = JSONObject().apply {
                        put("turno", qrJson.optString("turno", "N/A"))
                        put("maquina", qrJson.optString("maquina", "N/A"))
                        put("peso_final_kg", qrJson.optDouble("peso_final_kg", 0.0))
                    }.toString()

                    val payload = OperationPayload(
                        codigo_qr = mangaId,
                        tipo_operacion = "INGRESO-PROD",
                        locacion_origen = "ZONA_PRODUCCION",
                        locacion_destino = "ALMACEN_PRINCIPAL",
                        operario_id = "user@gmail.com",
                        metadatos = metadatosJson,
                        timestamp = currentTimestamp,
                        isSynced = false
                    )
                    payloadsToSave.add(payload)
                } catch (e: Exception) {
                    Log.e("PayloadBuilder", "Failed to parse QR JSON: $rawScan")
                }
            }
            payloadsToSave.forEach { Log.d("OperationPayload", it.toString()) }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ProduccionNuevaScreenPreview() {
    ProduccionNuevaScreen(onBackClick = {})
}
