package com.example.envasistema.ui.screens.ingresos

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.envasistema.ui.components.ScanningLayout
import com.example.envasistema.util.OperationPayload
import com.example.envasistema.util.getCurrentTimestampIso
import com.example.envasistema.util.parseCsvToJson
import org.json.JSONObject

@Composable
fun DevolucionNoArmadoScreen(onBackClick: () -> Unit) {
    ScanningLayout(
        title = "Devolución no Armado",
        subtitle = "INGRESOS",
        infoText = "Presione botón lateral para escanear código QR de la pieza / manga",
        onBackClick = onBackClick,
        onSaveClick = { scannedCodes ->
            val payloadsToSave = mutableListOf<OperationPayload>()
            val currentTimestamp = getCurrentTimestampIso()

            scannedCodes.forEach { rawScan ->
                try {
                    val qrJson = parseCsvToJson(rawScan)
                    val mangaId = qrJson.optString("manga-id", rawScan)
                    
                    val metadatosJson = JSONObject().apply {
                        put("operador", qrJson.optString("operador", "N/A"))
                        put("color", qrJson.optString("color", "N/A"))
                        put("n_op", qrJson.optString("n_op", "N/A"))
                    }.toString()

                    val payload = OperationPayload(
                        codigo_qr = mangaId,
                        tipo_operacion = "INGRESO-DEV",
                        locacion_origen = "ALMACEN_CLIENTE",
                        locacion_destino = "ZONA_DEVOLUCIONES",
                        operario_id = "user@gmail.com",
                        metadatos = metadatosJson,
                        timestamp = currentTimestamp,
                        isSynced = false
                    )
                    payloadsToSave.add(payload)
                } catch (e: Exception) {
                    Log.e("PayloadBuilder", "Failed to parse QR CSV: $rawScan")
                }
            }
            payloadsToSave.forEach { Log.d("OperationPayload", it.toString()) }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DevolucionNoArmadoScreenPreview() {
    DevolucionNoArmadoScreen(onBackClick = {})
}
