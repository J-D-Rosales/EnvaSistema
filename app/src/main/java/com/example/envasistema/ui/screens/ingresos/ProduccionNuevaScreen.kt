package com.example.envasistema.ui.screens.ingresos

import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.envasistema.ui.components.ScanningLayout
import com.example.envasistema.util.OperationPayload
import com.example.envasistema.util.getCurrentTimestampIso
import org.json.JSONObject

@Composable
fun ProduccionNuevaScreen(onBackClick: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var dialogText by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cerrar")
                }
            },
            title = { Text("Datos a Guardar (Test)") },
            text = { Text(dialogText) }
        )
    }

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

            // Actualizamos el estado para mostrar el Pop-up
            dialogText = if (payloadsToSave.isEmpty()) {
                "No se detectaron códigos válidos."
            } else {
                payloadsToSave.joinToString("\n\n---\n\n") { 
                    "Manga ID: ${it.codigo_qr}\nOp: ${it.tipo_operacion}\nMeta: ${it.metadatos}" 
                }
            }
            showDialog = true

            payloadsToSave.forEach { Log.d("OperationPayload", it.toString()) }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ProduccionNuevaScreenPreview() {
    ProduccionNuevaScreen(onBackClick = {})
}
