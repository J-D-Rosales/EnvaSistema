package com.example.envasistema.ui.screens.salidas

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Output
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.envasistema.ui.components.ScanningLayout

@Composable
fun VentaPTScreen(
    onBackClick: () -> Unit
) {
    ScanningLayout(
        title = "Venta de PT",
        subtitle = "SALIDAS",
        infoText = "Presione botón lateral para escanear código QR del producto a vender",
        onBackClick = onBackClick,
        onSaveClick = { /* TODO */ },
        saveButtonText = "Registrar Salida",
        saveButtonIcon = Icons.Default.Output
    )
}

@Preview(showBackground = true)
@Composable
fun VentaPTScreenPreview() {
    VentaPTScreen(onBackClick = {})
}
