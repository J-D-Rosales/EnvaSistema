package com.example.envasistema.ui.screens.salidas

import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.envasistema.ui.components.ScannedItemsStagingArea
import com.example.envasistema.ui.components.ScanningLayout
import com.example.envasistema.ui.viewmodel.OperationViewModel
import com.example.envasistema.ui.viewmodel.OperationViewModelFactory
import com.example.envasistema.util.OperationPayload
import com.example.envasistema.util.getCurrentTimestampIso
import com.example.envasistema.util.parseCsvToJson
import org.json.JSONObject

@Composable
fun MermaMolinoScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val viewModel: OperationViewModel = viewModel(
        factory = OperationViewModelFactory(context.applicationContext as android.app.Application)
    )

    val pendingScans = remember { mutableStateListOf<OperationPayload>() }

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
            title = { Text("Merma Registrada") },
            text = { Text(dialogText) }
        )
    }

    ScanningLayout(
        title = "Merma (Molino)",
        subtitle = "SALIDAS",
        infoText = "ATENCIÓN: Esta operación dará de baja permanentemente el inventario seleccionado.",
        onBackClick = onBackClick,
        saveButtonText = "Registrar Merma",
        saveButtonIcon = Icons.Default.ReportProblem,
        primaryColor = Color(0xFFB71C1C),
        infoCardBackground = Color(0xFFFFEBEE),
        infoIcon = Icons.Default.Warning,
        infoIconColor = Color(0xFFFBC02D),
        showInternalCounter = false,
        externalScannedCodes = pendingScans.map { it.codigo_qr },
        onCodeScanned = { rawScan ->
            try {
                val qrJson = parseCsvToJson(rawScan)
                val mangaId = qrJson.optString("manga-id", rawScan)
                
                if (pendingScans.none { it.codigo_qr == mangaId }) {
                    val metadatosJson = JSONObject().apply {
                        put("peso_final_kg", qrJson.optDouble("peso_final_kg", 0.0))
                        put("molde", qrJson.optString("molde", "N/A"))
                        put("maquina", qrJson.optString("maquina", "N/A"))
                    }.toString()

                    val payload = OperationPayload(
                        codigo_qr = mangaId,
                        tipo_operacion = "MERMA_MOLINO",
                        locacion_origen = "ALMACEN_PRINCIPAL",
                        locacion_destino = "MOLINO_DESTRUCCION",
                        operario_id = "user@gmail.com",
                        metadatos = metadatosJson,
                        timestamp = getCurrentTimestampIso(),
                        isSynced = false
                    )
                    pendingScans.add(0, payload)
                }
            } catch (e: Exception) {
                Log.e("MermaMolino", "Failed to parse QR: $rawScan")
            }
        },
        onSaveClick = {
            if (pendingScans.isNotEmpty()) {
                val batchCount = pendingScans.size
                pendingScans.forEach { payload ->
                    viewModel.saveOperation(payload)
                }
                dialogText = "Se ha registrado la merma de $batchCount ítems."
                showDialog = true
                pendingScans.clear()
                Toast.makeText(context, "Operación completada", Toast.LENGTH_SHORT).show()
            }
        },
        scannedItemsContent = {
            ScannedItemsStagingArea(
                pendingScans = pendingScans,
                onRemoveItem = { item -> pendingScans.remove(item) }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MermaMolinoScreenPreview() {
    MermaMolinoScreen(onBackClick = {})
}
