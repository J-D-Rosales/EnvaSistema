package com.example.envasistema.ui.screens.ingresos

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.envasistema.ui.components.ScanningLayout

@Composable
fun DevolucionNoArmadoScreen(
    onBackClick: () -> Unit
) {
    ScanningLayout(
        title = "Devolución no Armado",
        subtitle = "INGRESOS",
        infoText = "Presione botón lateral para escanear código QR de la pieza / manga",
        onBackClick = onBackClick,
        onSaveClick = { /* TODO */ }
    )
}

@Preview(showBackground = true)
@Composable
fun DevolucionNoArmadoScreenPreview() {
    DevolucionNoArmadoScreen(onBackClick = {})
}
