package com.example.envasistema.ui.screens.salidas

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.envasistema.ui.components.ScanningLayout

@Composable
fun MermaMolinoScreen(
    onBackClick: () -> Unit
) {
    ScanningLayout(
        title = "Merma (Molino)",
        subtitle = "SALIDAS",
        infoText = "ATENCIÓN: Esta operación dará de baja permanentemente el inventario seleccionado.",
        onBackClick = onBackClick,
        onSaveClick = { /* TODO */ },
        counterLabel = "Ítems a destruir",
        saveButtonText = "Registrar Merma",
        saveButtonIcon = Icons.Default.ReportProblem,
        primaryColor = Color(0xFFB71C1C), // Deep Red
        infoCardBackground = Color(0xFFFFEBEE),
        infoIcon = Icons.Default.Warning,
        infoIconColor = Color(0xFFFBC02D) // Yellow warning icon
    )
}

@Preview(showBackground = true)
@Composable
fun MermaMolinoScreenPreview() {
    MermaMolinoScreen(onBackClick = {})
}
