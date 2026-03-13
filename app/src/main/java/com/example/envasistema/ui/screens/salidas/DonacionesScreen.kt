package com.example.envasistema.ui.screens.salidas

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Output
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
fun DonacionesScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val viewModel: OperationViewModel = viewModel(
        factory = OperationViewModelFactory(context.applicationContext as android.app.Application)
    )

    val pendingScans = remember { mutableStateListOf<OperationPayload>() }
    var selectedDestino by remember { mutableStateOf("") }

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
            title = { Text("Donación Registrada") },
            text = { Text(dialogText) }
        )
    }

    ScanningLayout(
        title = "Donaciones",
        subtitle = "SALIDAS",
        infoText = "Presione botón lateral para escanear los productos a donar",
        onBackClick = onBackClick,
        saveButtonText = "Registrar Salida",
        saveButtonIcon = Icons.Default.Output,
        showInternalCounter = false,
        externalScannedCodes = pendingScans.map { it.codigo_qr },
        extraContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "DESTINO DE LA DONACIÓN",
                        color = Color(0xFF9E9E9E),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = null,
                            tint = Color(0xFF0061A6),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Centro de Costo / Motivo",
                            color = Color(0xFF455A64),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = " *", color = Color.Red)
                    }

                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (selectedDestino.isEmpty()) "— Seleccione un destino —" else selectedDestino,
                                color = if (selectedDestino.isEmpty()) Color(0xFFBDBDBD) else Color.Black,
                                fontSize = 15.sp
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color(0xFF757575)
                            )
                        }
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
                        put("destino", if (selectedDestino.isEmpty()) "N/A" else selectedDestino)
                        put("peso_final_kg", qrJson.optDouble("peso_final_kg", 0.0))
                    }.toString()

                    val payload = OperationPayload(
                        codigo_qr = mangaId,
                        tipo_operacion = "DONACIONES",
                        locacion_origen = "ALMACEN_PRINCIPAL",
                        locacion_destino = "DONACION_EXTERNA",
                        operario_id = "user@gmail.com",
                        metadatos = metadatosJson,
                        timestamp = getCurrentTimestampIso(),
                        isSynced = false
                    )
                    pendingScans.add(0, payload)
                }
            } catch (e: Exception) {
                Log.e("Donaciones", "Failed to parse QR: $rawScan")
            }
        },
        onSaveClick = {
            if (pendingScans.isNotEmpty()) {
                val batchCount = pendingScans.size
                pendingScans.forEach { payload ->
                    viewModel.saveOperation(payload)
                }
                dialogText = "Se han registrado $batchCount donaciones localmente."
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
fun DonacionesScreenPreview() {
    DonacionesScreen(onBackClick = {})
}
