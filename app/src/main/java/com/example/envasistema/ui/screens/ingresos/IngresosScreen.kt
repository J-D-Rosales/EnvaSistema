package com.example.envasistema.ui.screens.ingresos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.envasistema.ui.components.MenuCard
import com.example.envasistema.ui.components.SecondaryHeader

@Composable
fun IngresosScreen(
    onBackClick: () -> Unit,
    onProduccionNuevaClick: () -> Unit,
    onIngresoArmadoClick: () -> Unit,
    onDevolucionNoArmadoClick: () -> Unit,
    onDevolucionMercaderiaClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        SecondaryHeader(
            title = "Ingresos",
            subtitle = "Seleccione el tipo de ingreso",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "TIPO DE OPERACIÓN",
                color = Color(0xFF9E9E9E),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            MenuCard(
                title = "Producción Nueva",
                subtitle = "Ingreso de productos nuevos desde producción",
                icon = Icons.Default.AddBox,
                onClick = onProduccionNuevaClick
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            MenuCard(
                title = "Ingreso de Armado",
                subtitle = "Ingreso de piezas y mangas ya armadas",
                icon = Icons.Default.Sync,
                onClick = onIngresoArmadoClick
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            MenuCard(
                title = "Devolución no Armado",
                subtitle = "Devolución de piezas sin armar al stock",
                icon = Icons.AutoMirrored.Filled.RotateLeft,
                onClick = onDevolucionNoArmadoClick
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            MenuCard(
                title = "Devolución de Mercadería",
                subtitle = "Requiere N° de factura y datos del cliente",
                icon = Icons.Default.SouthWest,
                onClick = onDevolucionMercaderiaClick
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IngresosScreenPreview() {
    IngresosScreen(
        onBackClick = {},
        onProduccionNuevaClick = {},
        onIngresoArmadoClick = {},
        onDevolucionNoArmadoClick = {},
        onDevolucionMercaderiaClick = {}
    )
}
