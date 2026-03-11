package com.example.envasistema.ui.screens.salidas

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.envasistema.ui.components.ScanningLayout
import com.example.envasistema.util.OperationPayload
import com.example.envasistema.util.getCurrentTimestampIso
import org.json.JSONObject

@Composable
fun MermaMolinoScreen(onBackClick: () -> Unit) {
    ScanningLayout(
        title = "Merma (Molino)",
        subtitle = "SALIDAS",
        infoText = "ATENCIÓN: Esta operación dará de baja permanentemente el inventario seleccionado.",
        onBackClick = onBackClick,
        counterLabel = "Ítems a destruir",
        saveButtonText = "Registrar Merma",
        saveButtonIcon = Icons.Default.ReportProblem,
        primaryColor = Color(0xFFB71C1C),
        infoCardBackground = Color(0xFFFFEBEE),
        infoIcon = Icons.Default.Warning,
        infoIconColor = Color(0xFFFBC02D),
        onSaveClick = { scannedCodes ->
            val payloadsToSave = mutableListOf<OperationPayload>()
            val currentTimestamp = getCurrentTimestampIso()

            scannedCodes.forEach { rawScan ->
                try {
                    val qrJson = JSONObject(rawScan)
                    val mangaId = qrJson.optString("manga-id", rawScan)
                    
                    val metadatosJson = JSONObject().apply {
                        put("peso_final_kg", qrJson.optDouble("peso_final_kg", 0.0))
                        put("molde", qrJson.optString("molde", "N/A"))
                        put("maquina", qrJson.optString("maquina", "N/A"))
                    }.toString()

                    val payload = OperationPayload(
                        codigo_qr = mangaId,
                        tipo_operacion = "SAL-MERMA",
                        locacion_origen = "ALMACEN_PRINCIPAL",
                        locacion_destino = "MOLINO_DESTRUCCION",
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
fun MermaMolinoScreenPreview() {
    MermaMolinoScreen(onBackClick = {})
}
