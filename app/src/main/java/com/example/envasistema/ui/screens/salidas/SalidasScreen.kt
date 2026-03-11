package com.example.envasistema.ui.screens.salidas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.envasistema.ui.components.MenuCard
import com.example.envasistema.ui.components.SecondaryHeader

@Composable
fun SalidasScreen(
    onBackClick: () -> Unit,
    onVentaPTClick: () -> Unit,
    onArmarPaquetesClick: () -> Unit,
    onMermaMolinoClick: () -> Unit,
    onDonacionesClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        SecondaryHeader(
            title = "Salidas",
            subtitle = "Seleccione el tipo de salida",
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
                title = "Venta de PT",
                subtitle = "Salida de producto terminado por venta",
                icon = Icons.Default.ShoppingCart,
                onClick = {}/*onVentaPTClick*/
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            MenuCard(
                title = "Armar Paquetes",
                subtitle = "Agrupación de piezas en paquetes para despacho",
                icon = Icons.Default.Inventory,
                onClick = onArmarPaquetesClick
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            MenuCard(
                title = "Merma (Molino)",
                subtitle = "DESTRUCCIÓN de inventario - Acción irreversible",
                icon = Icons.Default.Warning,
                primaryColor = Color(0xFFD32F2F),
                iconBackgroundColor = Color(0xFFFFEBEE),
                iconTintColor = Color(0xFFD32F2F),
                rightIcon = Icons.Default.Warning,
                onClick = onMermaMolinoClick
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            MenuCard(
                title = "Donaciones",
                subtitle = "Salida a centros de costo externos o donación",
                icon = Icons.Default.FavoriteBorder,
                onClick = onDonacionesClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Warning Banner for Merma
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                shape = RoundedCornerShape(12.dp),
                border = Box(Modifier.border(1.dp, Color(0xFFFFCDD2), RoundedCornerShape(12.dp))).let { null }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "La operación de Merma es irreversible. Requiere confirmación del supervisor.",
                        color = Color(0xFFB71C1C),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 16.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SalidasScreenPreview() {
    SalidasScreen({}, {}, {}, {}, {})
}
