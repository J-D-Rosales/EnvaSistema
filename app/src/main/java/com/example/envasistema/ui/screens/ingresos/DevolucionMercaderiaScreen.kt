package com.example.envasistema.ui.screens.ingresos

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
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
fun DevolucionMercaderiaScreen(
    onBackClick: () -> Unit
) {
    var factura by remember { mutableStateOf("") }
    var cliente by remember { mutableStateOf("") }

    ScanningLayout(
        title = "Devolución de Mercadería",
        subtitle = "INGRESOS",
        infoText = "Presione botón lateral para escanear código QR de la mercadería devuelta",
        onBackClick = onBackClick,
        onSaveClick = { /* TODO */ },
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
                        text = "DATOS OBLIGATORIOS",
                        color = Color(0xFF9E9E9E),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Factura Field
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = Color(0xFF0061A6),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "N° de Factura",
                            color = Color(0xFF455A64),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = " *", color = Color.Red)
                    }
                    OutlinedTextField(
                        value = factura,
                        onValueChange = { factura = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        placeholder = { Text("Ej: FC-A-00012345", color = Color(0xFFBDBDBD)) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = Color(0xFF0061A6)
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Cliente Field
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF0061A6),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cliente",
                            color = Color(0xFF455A64),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = " *", color = Color.Red)
                    }
                    OutlinedTextField(
                        value = cliente,
                        onValueChange = { cliente = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        placeholder = { Text("Nombre o razón social", color = Color(0xFFBDBDBD)) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = Color(0xFF0061A6)
                        )
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DevolucionMercaderiaScreenPreview() {
    DevolucionMercaderiaScreen(onBackClick = {})
}
