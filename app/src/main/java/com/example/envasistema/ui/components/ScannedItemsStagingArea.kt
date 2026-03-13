package com.example.envasistema.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.envasistema.util.OperationPayload

/**
 * A reusable component that displays the "Staging Area" for scanned items.
 * It follows the State Hoisting pattern by accepting the list and a removal callback.
 */
@Composable
fun ScannedItemsStagingArea(
    pendingScans: List<OperationPayload>,
    onRemoveItem: (OperationPayload) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // 1. "Códigos escaneados" Title Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            androidx.compose.material3.Surface(
                color = Color(0xFF0061A6), // Matching primaryColor style
                shape = androidx.compose.foundation.shape.CircleShape,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text(
                        text = pendingScans.size.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
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

        Spacer(modifier = Modifier.height(16.dp))

        // 2. The list of cards
        if (pendingScans.isEmpty()) {
            Text(
                text = "No hay códigos escaneados",
                color = Color(0xFFBDBDBD),
                fontSize = 15.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            // Note: If this is used inside a scrollable Column (like ScanningLayout),
            // we use Column + forEach to avoid nested scrolling issues.
            pendingScans.forEach { item ->
                ScannedItemCard(
                    payload = item,
                    onRemove = { onRemoveItem(item) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
