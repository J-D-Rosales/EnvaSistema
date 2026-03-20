package com.example.envasistema.ui.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.envasistema.ui.components.HomeHeader
import com.example.envasistema.ui.components.MenuCard
import com.example.envasistema.ui.components.ShiftInfoRow
import com.amplifyframework.core.Amplify
import com.amplifyframework.auth.AuthUserAttributeKey
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    onIngresosClick: () -> Unit,
    onSalidasClick: () -> Unit,
    onMovimientosClick: () -> Unit,
    onTransformacionesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var userName by remember { mutableStateOf("Cargando...") }
    
    LaunchedEffect(Unit) {
        Amplify.Auth.fetchUserAttributes(
            { attributes ->
                val name = attributes.find { it.key == AuthUserAttributeKey.name() }?.value
                    ?: attributes.find { it.key == AuthUserAttributeKey.email() }?.value
                    ?: "Usuario"
                userName = name.uppercase()
            },
            { error ->
                Log.e("Auth", "Failed to fetch user attributes", error)
                userName = "INVITADO"
            }
        )
    }

    val currentDate = remember {
        SimpleDateFormat("EEE, d 'de' MMMM yyyy", Locale("es", "ES")).format(Date())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
    ) {
        // Header Blue Section
        HomeHeader(
            statusText = "ONLINE",
            userName = userName,
            onLogoutClick = onLogoutClick
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Date and Shift Info
            ShiftInfoRow(
                dateText = currentDate,
                shiftText = "Turno Actual"
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
