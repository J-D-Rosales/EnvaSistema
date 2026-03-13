package com.example.envasistema.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.envasistema.util.OperationPayload
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ScannedItemCard(
    payload: OperationPayload,
    onRemove: () -> Unit
) {
    // 1. Dynamic Name with Fallback
    val name = if (payload.pieza_nombre.isBlank()) "Pieza Desconocida" else payload.pieza_nombre

    // 2. Dynamic Weight from Payload
    val pesoDisplay = "${payload.peso_kg} kg"

    // Format ISO timestamp to local readable time (e.g., 12:26 PM)
    val hora = try {
        val zonedDateTime = ZonedDateTime.parse(payload.timestamp)
        val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        "--:--"
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Color(0xFFE3F2FD),
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = Color(0xFF0061A6),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF212121)
                )
                Text(
                    text = "Peso: $pesoDisplay",
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )
            }
            
            Text(
                text = hora,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF9E9E9E),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFB71C1C)
                )
            }
        }
    }
}
