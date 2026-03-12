package com.example.envasistema.ui.screens.ingresos

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.envasistema.ui.components.ScanningLayout
import com.example.envasistema.ui.viewmodel.OperationViewModel
import com.example.envasistema.ui.viewmodel.OperationViewModelFactory
import com.example.envasistema.util.OperationPayload
import com.example.envasistema.util.getCurrentTimestampIso
import com.example.envasistema.util.parseCsvToJson
import org.json.JSONObject

@Composable
fun ProduccionNuevaScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val viewModel: OperationViewModel = viewModel(
        factory = OperationViewModelFactory(context.applicationContext as android.app.Application)
    )

    var showDialog by remember { mutableStateOf(false) }
    var dialogText by remember { mutableStateOf("") }
    
    // Key to trigger clearing the ScanningLayout state
    var scanningLayoutKey by remember { mutableIntStateOf(0) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cerrar")
                }
            },
            title = { Text("Datos Guardados Localmente") },
            text = { Text(dialogText) }
        )
    }

    key(scanningLayoutKey) {
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
                        val qrJson = parseCsvToJson(rawScan)
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
                        
                        // Save to Room database via ViewModel
                        viewModel.saveOperation(payload)
                        
                    } catch (e: Exception) {
                        Log.e("PayloadBuilder", "Failed to parse QR CSV: $rawScan")
                    }
                }

                if (payloadsToSave.isNotEmpty()) {
                    dialogText = payloadsToSave.joinToString("\n\n---\n\n") { 
                        "Manga ID: ${it.codigo_qr}\nOp: ${it.tipo_operacion}\nMeta: ${it.metadatos}" 
                    }
                    showDialog = true
                    Toast.makeText(context, "Guardado localmente", Toast.LENGTH_SHORT).show()
                    
                    // Reset the ScanningLayout state by changing the key
                    scanningLayoutKey++
                } else {
                    Toast.makeText(context, "No se detectaron códigos válidos", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProduccionNuevaScreenPreview() {
    ProduccionNuevaScreen(onBackClick = {})
}
