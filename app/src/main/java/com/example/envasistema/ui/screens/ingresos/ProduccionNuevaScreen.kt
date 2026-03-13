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
import com.example.envasistema.util.parseQrToPayload
import org.json.JSONObject

@Composable
fun ProduccionNuevaScreen(onBackClick: () -> Unit) {
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
        title = "Producción Nueva",
        subtitle = "INGRESOS",
        infoText = "Presione botón lateral para escanear código QR de la pieza / manga",
        onBackClick = onBackClick,
        showInternalCounter = false,
        externalScannedCodes = pendingScans.map { it.codigo_qr },
        onCodeScanned = { rawScan ->
            // Use the robust parser instead of manual JSON conversion
            val payload = parseQrToPayload(
                rawScan = rawScan,
                tipoOperacion = "INGRESO-PROD",
                locacionOrigen = "ZONA_PRODUCCION",
                locacionDestino = "ALMACEN_PRINCIPAL",
                operarioId = "user@gmail.com"
            )

            if (payload != null) {
                if (pendingScans.none { it.codigo_qr == payload.codigo_qr }) {
                    pendingScans.add(0, payload)
                }
            } else {
                Log.e("ProduccionNuevaScreen", "Failed to parse QR or insufficient fields: $rawScan")
                Toast.makeText(context, "QR inválido o incompleto", Toast.LENGTH_SHORT).show()
            }
        },
        onSaveClick = {
            if (pendingScans.isNotEmpty()) {
                val batchCount = pendingScans.size
                pendingScans.forEach { payload ->
                    viewModel.saveOperation(payload)
                }
                dialogText = "Se han registrado $batchCount códigos en la base de datos local."
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
fun ProduccionNuevaScreenPreview() {
    ProduccionNuevaScreen(onBackClick = {})
}
