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
import com.example.envasistema.ui.components.ScannedItemsStagingArea
import com.example.envasistema.ui.components.ScanningLayout
import com.example.envasistema.ui.viewmodel.OperationViewModel
import com.example.envasistema.ui.viewmodel.OperationViewModelFactory
import com.example.envasistema.util.OperationPayload
import com.example.envasistema.util.getCurrentTimestampIso
import com.example.envasistema.util.parseCsvToJson
import org.json.JSONObject

@Composable
fun DevolucionNoArmadoScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val viewModel: OperationViewModel = viewModel(
        factory = OperationViewModelFactory(context.applicationContext as android.app.Application)
    )

    // State Hoisting: Local UI state for the reusable Staging Area
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
            title = { Text("Datos Guardados Localmente") },
            text = { Text(dialogText) }
        )
    }

    ScanningLayout(
        title = "Devolución no Armado",
        subtitle = "INGRESOS",
        infoText = "Presione botón lateral para escanear código QR de la pieza / manga",
        onBackClick = onBackClick,
        showInternalCounter = false,
        externalScannedCodes = pendingScans.map { it.codigo_qr },
        onCodeScanned = { rawScan ->
            try {
                val qrJson = parseCsvToJson(rawScan)
                val mangaId = qrJson.optString("manga-id", rawScan)
                
                if (pendingScans.none { it.codigo_qr == mangaId }) {
                    val metadatosJson = JSONObject().apply {
                        put("operador", qrJson.optString("operador", "N/A"))
                        put("color", qrJson.optString("color", "N/A"))
                        put("n_op", qrJson.optString("n_op", "N/A"))
                    }.toString()

                    val payload = OperationPayload(
                        codigo_qr = mangaId,
                        tipo_operacion = "DEVOLUCION_NO_ARMADO",
                        locacion_origen = "ALMACEN_CLIENTE",
                        locacion_destino = "ZONA_DEVOLUCIONES",
                        operario_id = "user@gmail.com",
                        metadatos = metadatosJson,
                        timestamp = getCurrentTimestampIso(),
                        isSynced = false
                    )
                    pendingScans.add(0, payload)
                }
            } catch (e: Exception) {
                Log.e("DevolucionNoArmado", "Failed to parse QR: $rawScan")
            }
        },
        onSaveClick = {
            if (pendingScans.isNotEmpty()) {
                val batchCount = pendingScans.size
                pendingScans.forEach { payload ->
                    viewModel.saveOperation(payload)
                }
                dialogText = "Se han registrado $batchCount devoluciones localmente."
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
fun DevolucionNoArmadoScreenPreview() {
    DevolucionNoArmadoScreen(onBackClick = {})
}
