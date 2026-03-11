package com.example.envasistema.ui.screens.movimientos

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.envasistema.util.getCurrentTimestampIso
import org.json.JSONObject

@Composable
fun TransferenciaInventarioScreen(onBackClick: () -> Unit) {
    val currentLocation by remember { mutableStateOf("ENVA") }

    ScanningLayout(
        title = "Transferencia de Inventario",
        subtitle = "MOVIMIENTOS INTERNOS",
        infoText = "RECEPCIÓN — Escaneo de Productos\nEscanee los códigos QR de los productos. La transferencia se completa cuando otro terminal escanea el mismo producto.",
        onBackClick = onBackClick,
        counterLabel = "Productos escaneados",
        saveButtonText = "Confirmar Transferencia",
        saveButtonIcon = Icons.Default.Inventory2,
        extraContent = {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp),
                border = Box(Modifier.border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))).let { null }
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
        onSaveClick = { scannedCodes ->
            val payloadsToSave = mutableListOf<OperationPayload>()
            val currentTimestamp = getCurrentTimestampIso()

            scannedCodes.forEach { rawScan ->
                try {
                    val qrJson = JSONObject(rawScan)
                    val mangaId = qrJson.optString("manga-id", rawScan)
                    
                    val metadatosJson = JSONObject().apply {
                        put("n_op", qrJson.optString("n_op", "N/A"))
                        put("operador", qrJson.optString("operador", "N/A"))
                    }.toString()

                    val payload = OperationPayload(
                        codigo_qr = mangaId,
                        tipo_operacion = "MOV-INTERNO",
                        locacion_origen = currentLocation,
                        locacion_destino = "ZONA_TRANSITO",
                        operario_id = "user@gmail.com",
                        metadatos = metadatosJson,
                        timestamp = currentTimestamp,
                        isSynced = false
                    )
                    payloadsToSave.add(payload)
                } catch (e: Exception) {
                    Log.e("PayloadBuilder", "Failed to parse QR JSON: $rawScan")
                }
            }
            payloadsToSave.forEach { Log.d("OperationPayload", it.toString()) }
        }
    )
}
