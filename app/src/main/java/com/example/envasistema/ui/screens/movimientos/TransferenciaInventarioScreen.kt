package com.example.envasistema.ui.screens.movimientos

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.envasistema.ui.components.ScanningLayout

@Composable
fun TransferenciaInventarioScreen(
    onBackClick: () -> Unit
) {
    // We assume the app knows the current location, default to "ENVA"
    val currentLocation by remember { mutableStateOf("ENVA") }

    ScanningLayout(
        title = "Transferencia de Inventario",
        subtitle = "MOVIMIENTOS INTERNOS",
        infoText = "RECEPCIÓN — Escaneo de Productos\nEscanee los códigos QR de los productos. La transferencia se completa cuando otro terminal escanea el mismo producto.",
        onBackClick = onBackClick,
        onSaveClick = { /* TODO */ },
        counterLabel = "Productos escaneados",
        saveButtonText = "Confirmar Transferencia",
        saveButtonIcon = Icons.Default.Inventory2,
        extraContent = {
            // My Current Location Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp),
                border = Box(Modifier.border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))).let { null }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFF0061A6),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Mi Ubicación Actual",
                            color = Color(0xFF455A64),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Static Location Display Box
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF0061A6)),
                            width = 2.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentLocation,
                                color = Color(0xFF0D47A1),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
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
