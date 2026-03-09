package com.example.envasistema.ui.screens.transformaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.envasistema.ui.components.ScanningLayout

@Composable
fun DesarmarPaquetesScreen(
    onBackClick: () -> Unit
) {
    ScanningLayout(
        title = "Desarmar Paquetes",
        subtitle = "TRANSFORMACIONES",
        infoText = "Escanee el código QR del PT a desarmar con el botón lateral del terminal",
        onBackClick = onBackClick,
        onSaveClick = { /* TODO */ },
        counterLabel = "PT a desarmar",
        saveButtonText = "Ejecutar Desarmado",
        saveButtonIcon = Icons.Default.Build,
        primaryColor = Color(0xFF455A64), // Dark Grey as per image
        infoCardBackground = Color(0xFFE1F5FE),
        extraContent = {
            // "What does this operation do?" Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0xFF455A64),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inventory2,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "¿Qué hace esta operación?",
                                color = Color(0xFF455A64),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Escanee el Producto Terminado (PT) para dar de baja su código y reingresar las piezas que lo componen al inventario de partes.",
                                color = Color(0xFF757575),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Steps
                    StepItem(number = "1", text = "Escanee el código QR del Paquete PT a desarmar")
                    Spacer(modifier = Modifier.height(8.dp))
                    StepItem(number = "2", text = "El sistema dará de baja el código del PT")
                    Spacer(modifier = Modifier.height(8.dp))
                    StepItem(number = "3", text = "Las piezas serán reingresadas automáticamente al stock", isLast = true)
                }
            }
        }
    )
}

@Composable
fun StepItem(number: String, text: String, isLast: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            color = if (number == "3") Color(0xFF4CAF50) else Color(0xFF0061A6),
            shape = CircleShape,
            modifier = Modifier.size(20.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = number, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, color = Color(0xFF757575), fontSize = 12.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun DesarmarPaquetesScreenPreview() {
    DesarmarPaquetesScreen(onBackClick = {})
}
