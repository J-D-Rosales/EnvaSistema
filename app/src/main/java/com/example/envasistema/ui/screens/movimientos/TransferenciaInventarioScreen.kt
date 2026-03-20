package com.example.envasistema.ui.screens.movimientos

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
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
import com.example.envasistema.util.parseQrToPayload
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferenciaInventarioScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val viewModel: OperationViewModel = viewModel(
        factory = OperationViewModelFactory(context.applicationContext as android.app.Application)
    )

    val pendingScans = remember { mutableStateListOf<OperationPayload>() }
    
    // Physical locations list
    val locations = listOf("CUAVES", "MUEBLE", "JOSE GALVEZ", "ENVA")
    var selectedLocation by remember { mutableStateOf("ENVA") }
    var expanded by remember { mutableStateOf(false) }
    
    // UI State for Transfer Step
    var selectedStep by remember { mutableStateOf("INICIAR") } // "INICIAR" or "FINALIZAR"

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
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                // Location Selection Component
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, tint = Color(0xFF0061A6), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Seleccionar Ubicación Física", color = Color(0xFF455A64), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedLocation,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color(0xFF0061A6),
                                    unfocusedBorderColor = Color(0xFFBDBDBD)
                                ),
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                textStyle = LocalTextStyle.current.copy(
                                    color = Color(0xFF0D47A1),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                locations.forEach { location ->
                                    DropdownMenuItem(
                                        text = { Text(location) },
                                        onClick = {
                                            selectedLocation = location
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        onCodeScanned = { rawScan ->
            val payload = parseQrToPayload(
                rawScan = rawScan,
                tipoOperacion = "MOVIMIENTOS",
                locacionOrigen = selectedLocation, // Using selected location
                locacionDestino = "TRANSITO",
                operarioId = "user@gmail.com"
            )

            if (payload != null) {
                if (pendingScans.none { it.codigo_qr == payload.codigo_qr }) {
                    pendingScans.add(0, payload)
                }
            } else {
                Log.e("TransferenciaScreen", "Failed to parse QR or insufficient fields: $rawScan")
                Toast.makeText(context, "QR inválido o incompleto", Toast.LENGTH_SHORT).show()
            }
        },
        onSaveClick = {
            if (pendingScans.isNotEmpty()) {
                val batchCount = pendingScans.size
                pendingScans.forEach { payload ->
                    // REVISED MAPPING LOGIC:
                    // If INICIAR: Origin = selectedLoc, Destination = "TRANSITO"
                    // If FINALIZAR: Origin = "TRANSITO", Destination = selectedLoc
                    val finalPayload = payload.copy(
                        locacion_origen = if (selectedStep == "INICIAR") selectedLocation else "TRANSITO",
                        locacion_destino = if (selectedStep == "INICIAR") "TRANSITO" else selectedLocation
                    )
                    viewModel.saveOperation(finalPayload)
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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // UI Update: Transfer Step Toggle
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F3F4)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, 
                        modifier = Modifier.clickable { selectedStep = "INICIAR" }
                    ) {
                        RadioButton(selected = selectedStep == "INICIAR", onClick = { selectedStep = "INICIAR" })
                        Text("Iniciar Traslado", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically, 
                        modifier = Modifier.clickable { selectedStep = "FINALIZAR" }
                    ) {
                        RadioButton(selected = selectedStep == "FINALIZAR", onClick = { selectedStep = "FINALIZAR" })
                        Text("Finalizar Traslado", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TransferenciaInventarioScreenPreview() {
    TransferenciaInventarioScreen(onBackClick = {})
}
