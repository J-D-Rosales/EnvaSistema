package com.example.envasistema.ui.screens.movimientos

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun TransferenciaInventarioScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val viewModel: OperationViewModel = viewModel(
        factory = OperationViewModelFactory(context.applicationContext as android.app.Application)
    )

    val pendingScans = remember { mutableStateListOf<OperationPayload>() }
    val currentLocation by remember { mutableStateOf("ENVA") }

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
            title = { Text("Transferencia Registrada") },
            text = { Text(dialogText) }
        )
    }

    ScanningLayout(
        title = "Transferencia de Inventario",
        subtitle = "MOVIMIENTOS INTERNOS",
        infoText = "RECEPCIÓN — Escaneo de Productos\nEscanee los códigos QR de los productos. La transferencia se completa cuando otro terminal escanea el mismo producto.",
        onBackClick = onBackClick,
        saveButtonText = "Confirmar Transferencia",
        saveButtonIcon = Icons.Default.Inventory2,
        showInternalCounter = false,
        externalScannedCodes = pendingScans.map { it.codigo_qr },
        extraContent = {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, tint = Color(0xFF0061A6), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Mi Ubicación Actual", color = Color(0xFF455A64), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF0061A6)), width = 2.dp)
                    ) {
                        Text(currentLocation, modifier = Modifier.padding(16.dp), color = Color(0xFF0D47A1), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        onCodeScanned = { rawScan ->
            try {
                val qrJson = parseCsvToJson(rawScan)
                val mangaId = qrJson.optString("manga-id", rawScan)
                
                if (pendingScans.none { it.codigo_qr == mangaId }) {
                    val metadatosJson = JSONObject().apply {
                        put("n_op", qrJson.optString("n_op", "N/A"))
                        put("operador", qrJson.optString("operador", "N/A"))
                    }.toString()

                    val payload = OperationPayload(
                        codigo_qr = mangaId,
                        tipo_operacion = "MOVIMIENTOS",
                        locacion_origen = currentLocation,
                        locacion_destino = "ZONA_TRANSITO",
                        operario_id = "user@gmail.com",
                        metadatos = metadatosJson,
                        timestamp = getCurrentTimestampIso(),
                        isSynced = false
                    )
                    pendingScans.add(0, payload)
                }
            } catch (e: Exception) {
                Log.e("Transferencia", "Failed to parse QR: $rawScan")
            }
        },
        onSaveClick = {
            if (pendingScans.isNotEmpty()) {
                val batchCount = pendingScans.size
                pendingScans.forEach { payload ->
                    viewModel.saveOperation(payload)
                }
                dialogText = "Se han registrado $batchCount transferencias localmente."
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
fun TransferenciaInventarioScreenPreview() {
    TransferenciaInventarioScreen(onBackClick = {})
}
