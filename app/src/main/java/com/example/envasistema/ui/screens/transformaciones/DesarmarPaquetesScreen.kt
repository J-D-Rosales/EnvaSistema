package com.example.envasistema.ui.screens.transformaciones

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Inventory2
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
fun DesarmarPaquetesScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val viewModel: OperationViewModel = viewModel(
        factory = OperationViewModelFactory(context.applicationContext as android.app.Application)
    )

    val pendingScans = remember { mutableStateListOf<OperationPayload>() }
    var tipoTransformacion by remember { mutableStateOf("") }

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
            title = { Text("Transformación Registrada") },
            text = { Text(dialogText) }
        )
    }

    ScanningLayout(
        title = "Desarmar Paquetes",
        subtitle = "TRANSFORMACIONES",
        infoText = "Escanee el código QR del PT a desarmar con el botón lateral del terminal",
        onBackClick = onBackClick,
        saveButtonText = "Ejecutar Desarmado",
        saveButtonIcon = Icons.Default.Build,
        primaryColor = Color(0xFF455A64),
        infoCardBackground = Color(0xFFE1F5FE),
        showInternalCounter = false,
        externalScannedCodes = pendingScans.map { it.codigo_qr },
        extraContent = {
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(color = Color(0xFF455A64), shape = RoundedCornerShape(8.dp), modifier = Modifier.size(40.dp)) {
                                Icon(Icons.Default.Inventory2, null, tint = Color.White, modifier = Modifier.padding(8.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("¿Qué hace esta operación?", color = Color(0xFF455A64), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Text("Escanee el Producto Terminado (PT) para dar de baja su código y reingresar las piezas que lo componen al inventario de partes.", color = Color(0xFF757575), fontSize = 12.sp, lineHeight = 16.sp)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = tipoTransformacion,
                    onValueChange = { tipoTransformacion = it },
                    label = { Text("Tipo de Transformación (Cortado, Sellado...)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        onCodeScanned = { rawScan ->
            try {
                val qrJson = parseCsvToJson(rawScan)
                val mangaId = qrJson.optString("manga-id", rawScan)
                
                if (pendingScans.none { it.codigo_qr == mangaId }) {
                    val metadata = JSONObject().apply {
                        put("tipo_transformacion", tipoTransformacion)
                        put("nro_op", qrJson.optString("nro_op", "N/A"))
                    }.toString()

                    val payload = OperationPayload(
                        codigo_qr = mangaId,
                        tipo_operacion = "TRANSFORMACIONES",
                        locacion_origen = "ALMACEN_PT",
                        locacion_destino = "ALMACEN_PARTES",
                        operario_id = "user@gmail.com",
                        metadatos = metadata,
                        timestamp = getCurrentTimestampIso(),
                        isSynced = false
                    )
                    pendingScans.add(0, payload)
                }
            } catch (e: Exception) {
                Log.e("DesarmarPaquetes", "Failed to parse QR: $rawScan")
            }
        },
        onSaveClick = {
            if (pendingScans.isNotEmpty()) {
                val batchCount = pendingScans.size
                pendingScans.forEach { payload ->
                    viewModel.saveOperation(payload)
                }
                dialogText = "Se han registrado $batchCount transformaciones localmente."
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
fun DesarmarPaquetesScreenPreview() {
    DesarmarPaquetesScreen(onBackClick = {})
}
