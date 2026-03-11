package com.example.envasistema.ui.screens.transformaciones

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.envasistema.ui.components.ScanningLayout
import com.example.envasistema.util.OperationPayload
import com.example.envasistema.util.extractMangaIdFromScan
import com.example.envasistema.util.getCurrentTimestampIso
import org.json.JSONObject

@Composable
fun DesarmarPaquetesScreen(onBackClick: () -> Unit) {
    var tipoTransformacion by remember { mutableStateOf("") }

    ScanningLayout(
        title = "Desarmar Paquetes",
        subtitle = "TRANSFORMACIONES",
        infoText = "Escanee el código QR del PT a desarmar con el botón lateral del terminal",
        onBackClick = onBackClick,
        counterLabel = "PT a desarmar",
        saveButtonText = "Ejecutar Desarmado",
        saveButtonIcon = Icons.Default.Build,
        primaryColor = Color(0xFF455A64),
        infoCardBackground = Color(0xFFE1F5FE),
        extraContent = {
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                // Info Card (Original UI)
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
        onSaveClick = { scannedList ->
            val metadata = JSONObject().apply {
                put("tipo_transformacion", tipoTransformacion)
            }.toString()

            val payloads = scannedList.map { raw ->
                OperationPayload(
                    codigo_qr = extractMangaIdFromScan(raw),
                    tipo_operacion = "TRANSFORMACION",
                    locacion_origen = "ALMACEN_PT",
                    locacion_destino = "ALMACEN_PARTES",
                    operario_id = "user@gmail.com",
                    metadatos = metadata,
                    timestamp = getCurrentTimestampIso()
                )
            }
            payloads.forEach { Log.d("OperationPayload", it.toString()) }
        }
    )
}
