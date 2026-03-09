package com.example.envasistema.ui.screens.ingresos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.envasistema.ui.components.SecondaryHeader

@Composable
fun ProduccionNuevaScreen(
    onBackClick: () -> Unit
) {
    var scannCount by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        SecondaryHeader(
            title = "Producción Nueva",
            subtitle = "INGRESOS",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Info Alert Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFE1F5FE),
                shape = RoundedCornerShape(12.dp),
                border = Box(Modifier.border(2.dp, Color(0xFF0061A6), RoundedCornerShape(12.dp))).let { null } 
            ) {
                // Manually drawing the left blue border effect
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.width(6.dp).height(60.dp).background(Color(0xFF0061A6)))
                    
                    Icon(
                        imageVector = Icons.Default.RadioButtonChecked,
                        contentDescription = null,
                        tint = Color(0xFF0061A6),
                        modifier = Modifier.padding(horizontal = 12.dp).size(24.dp)
                    )
                    Text(
                        text = "Presione botón lateral para escanear código QR de la pieza / manga",
                        color = Color(0xFF0061A6),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(end = 16.dp, top = 8.dp, bottom = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Scan Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(1.dp, Color(0xFFBDBDBD), RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { scannCount++ },
                contentAlignment = Alignment.Center
            ) {
                // Dashed border effect would need a custom modifier, using solid for now
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        color = Color(0xFFEEEEEE),
                        shape = CircleShape,
                        modifier = Modifier.size(80.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = Color(0xFF9E9E9E),
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Presione el botón lateral del terminal para escanear",
                        color = Color(0xFF607D8B),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "(Toque aquí para simular escaneo)",
                        color = Color(0xFF9E9E9E),
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Scanned Codes Counter
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xFF0061A6),
                    shape = CircleShape,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = scannCount.toString(), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Códigos escaneados",
                    color = Color(0xFF455A64),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            // Empty State
            if (scannCount == 0) {
                Text(
                    text = "No hay códigos escaneados",
                    color = Color(0xFFBDBDBD),
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer Button
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (scannCount > 0) Color(0xFF0061A6) else Color(0xFFB0BEC5),
                    contentColor = Color.White
                )
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Guardar Ingreso", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProduccionNuevaScreenPreview() {
    ProduccionNuevaScreen(onBackClick = {})
}
