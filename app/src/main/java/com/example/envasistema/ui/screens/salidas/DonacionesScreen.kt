package com.example.envasistema.ui.screens.salidas

import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.envasistema.ui.components.ScanningLayout

@Composable
fun DonacionesScreen(
    onBackClick: () -> Unit
) {
    var selectedDestino by remember { mutableStateOf("") }

    ScanningLayout(
        title = "Donaciones",
        subtitle = "SALIDAS",
        infoText = "Presione botón lateral para escanear los productos a donar",
        onBackClick = onBackClick,
        onSaveClick = { /* TODO */ },
        counterLabel = "Productos a donar",
        saveButtonText = "Registrar Salida",
        saveButtonIcon = Icons.Default.Output,
        extraContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp),
                border = Box(Modifier.border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))).let { null }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
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

                    // Simulated Dropdown
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = Color.Transparent
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE0E0E0)))
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
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DonacionesScreenPreview() {
    DonacionesScreen(onBackClick = {})
}
