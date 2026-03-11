package com.example.envasistema.ui.screens.salidas

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Output
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.envasistema.ui.components.ScanningLayout
import com.example.envasistema.util.OperationPayload
import com.example.envasistema.util.getCurrentTimestampIso
import org.json.JSONObject

@Composable
fun ArmarPaquetesScreen(onBackClick: () -> Unit) {
    ScanningLayout(
        title = "Armar Paquetes",
        subtitle = "SALIDAS",
        infoText = "Presione botón lateral para escanear las piezas que conformarán el paquete",
        onBackClick = onBackClick,
        saveButtonText = "Registrar Salida",
        saveButtonIcon = Icons.Default.Output,
        onSaveClick = { scannedCodes ->
            val payloadsToSave = mutableListOf<OperationPayload>()
            val currentTimestamp = getCurrentTimestampIso()

            scannedCodes.forEach { rawScan ->
                try {
                    val qrJson = JSONObject(rawScan)
                    val mangaId = qrJson.optString("manga-id", rawScan)
                    
                    val metadatosJson = JSONObject().apply {
                        put("n_op", qrJson.optString("n_op", "N/A"))
                        put("fecha_de_ot", qrJson.optString("fecha_de_ot", "N/A"))
                    }.toString()

                    val payload = OperationPayload(
                        codigo_qr = mangaId,
                        tipo_operacion = "SAL-ARMAR",
                        locacion_origen = "ALMACEN_PARTES",
                        locacion_destino = "ZONA_DESPACHO",
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
fun ArmarPaquetesScreenPreview() {
    ArmarPaquetesScreen(onBackClick = {})
}
