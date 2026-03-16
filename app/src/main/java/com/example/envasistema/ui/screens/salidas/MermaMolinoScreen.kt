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
import com.example.envasistema.util.parseQrToPayload
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
            val payload = parseQrToPayload(
                rawScan = rawScan,
                tipoOperacion = "MERMA_MOLINO",
                locacionOrigen = "ALMACEN_PRINCIPAL",
                locacionDestino = "MOLINO_DESTRUCCION",
                operarioId = "user@gmail.com"
            )

            if (payload != null) {
                if (pendingScans.none { it.codigo_qr == payload.codigo_qr }) {
                    pendingScans.add(0, payload)
                }
            } else {
                Log.e("MermaMolinoScreen", "Failed to parse QR or insufficient fields: $rawScan")
                Toast.makeText(context, "QR inválido o incompleto", Toast.LENGTH_SHORT).show()
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
