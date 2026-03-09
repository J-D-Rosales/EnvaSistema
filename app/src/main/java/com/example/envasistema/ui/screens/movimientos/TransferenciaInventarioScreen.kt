package com.example.envasistema.ui.screens.movimientos

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SyncAlt
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
    var origen by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    
    val locations = listOf("CUAVES", "MUEBLE", "JOSE GALVEZ", "ENVA")
    
    var showOrigenMenu by remember { mutableStateOf(false) }
    var showDestinoMenu by remember { mutableStateOf(false) }

    ScanningLayout(
        title = "Transferencia de Inventario",
        subtitle = "MOVIMIENTOS INTERNOS",
        infoText = "Escanee los códigos QR de las piezas o mangas a transferir",
        onBackClick = onBackClick,
        onSaveClick = { /* TODO */ },
        counterLabel = "Ítems a transferir",
        saveButtonText = "Confirmar Transferencia",
        saveButtonIcon = Icons.Default.SyncAlt,
        isSaveButtonEnabled = { count ->
            count > 0 && origen.isNotEmpty() && destino.isNotEmpty() && origen != destino
        },
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
                        text = "UBICACIONES",
                        color = Color(0xFF9E9E9E),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Origen Column
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Origen", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF455A64))
                                Text(text = " *", color = Color.Red)
                            }
                            
                            Box {
                                OutlinedCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                        .clickable { showOrigenMenu = true },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent),
                                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE0E0E0)))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (origen.isEmpty()) "— Origen —" else origen,
                                            color = if (origen.isEmpty()) Color(0xFFBDBDBD) else Color.Black,
                                            fontSize = 14.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Icon(Icons.Default.ArrowDropDown, null, tint = Color(0xFF757575))
                                    }
                                }
                                
                                DropdownMenu(
                                    expanded = showOrigenMenu,
                                    onDismissRequest = { showOrigenMenu = false }
                                ) {
                                    locations.forEach { location ->
                                        DropdownMenuItem(
                                            text = { Text(location) },
                                            onClick = {
                                                origen = location
                                                showOrigenMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        
                        // Transfer Icon
                        Icon(
                            imageVector = Icons.Default.SyncAlt,
                            contentDescription = null,
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 24.dp).size(20.dp)
                        )
                        
                        // Destino Column
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFF0061A6),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Destino", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF455A64))
                                Text(text = " *", color = Color.Red)
                            }
                            
                            Box {
                                OutlinedCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                        .clickable { showDestinoMenu = true },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent),
                                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE0E0E0)))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (destino.isEmpty()) "— Destino —" else destino,
                                            color = if (destino.isEmpty()) Color(0xFFBDBDBD) else Color.Black,
                                            fontSize = 14.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Icon(Icons.Default.ArrowDropDown, null, tint = Color(0xFF757575))
                                    }
                                }
                                
                                DropdownMenu(
                                    expanded = showDestinoMenu,
                                    onDismissRequest = { showDestinoMenu = false }
                                ) {
                                    locations.forEach { location ->
                                        DropdownMenuItem(
                                            text = { Text(location) },
                                            onClick = {
                                                destino = location
                                                showDestinoMenu = false
                                            }
                                        )
                                    }
                                }
                            }
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
