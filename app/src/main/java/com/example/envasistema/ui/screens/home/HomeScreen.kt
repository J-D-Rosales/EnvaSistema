package com.example.envasistema.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.envasistema.ui.components.HomeHeader
import com.example.envasistema.ui.components.MenuCard
import com.example.envasistema.ui.components.ShiftInfoRow

@Composable
fun HomeScreen(
    onIngresosClick: () -> Unit,
    onSalidasClick: () -> Unit,
    onMovimientosClick: () -> Unit,
    onTransformacionesClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
    ) {
        // Header Blue Section
        HomeHeader(
            statusText = "C66 · ONLINE",
            userName = "J. PÉREZ",
            onLogoutClick = { /* TODO */ }
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Date and Shift Info
            ShiftInfoRow(
                dateText = "Lun, 9 de Marzo 2026",
                shiftText = "Turno Mañana"
            )

            // Menu Items List
            MenuCard(
                title = "INGRESOS",
                subtitle = "Producción, armados y devoluciones",
                icon = Icons.Default.AddBox,
                onClick = onIngresosClick
            )
            MenuCard(
                title = "SALIDAS",
                subtitle = "Ventas, paquetes, mermas y donaciones",
                icon = Icons.Default.DoubleArrow,
                onClick = onSalidasClick
            )
            MenuCard(
                title = "MOVIMIENTOS",
                subtitle = "Transferencia entre ubicaciones",
                icon = Icons.Default.SwapHoriz,
                onClick = onMovimientosClick
            )
            MenuCard(
                title = "TRANSFORMACIONES",
                subtitle = "Desarmar paquetes de producto terminado",
                icon = Icons.Default.Build,
                onClick = onTransformacionesClick
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1080px,height=1920px,dpi=400")
@Composable
fun HomeScreenPreview() {
    HomeScreen({}, {}, {}, {})
}
