package com.example.envasistema.ui.screens.ingresos

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.envasistema.ui.components.ScanningLayout

@Composable
fun ProduccionNuevaScreen(
    onBackClick: () -> Unit
) {
    ScanningLayout(
        title = "Producción Nueva",
        subtitle = "INGRESOS",
        infoText = "Presione botón lateral para escanear código QR de la pieza / manga",
        onBackClick = onBackClick,
        onSaveClick = { scannedCodes ->
            // TODO: Implement save logic for production codes
            println("=====================================")
            println("BINGO! THE BUTTON WAS CLICKED!")
            println("Total items scanned: ${scannedCodes.size}")

            // We loop through the 'scannedCodes' list to print each one
            scannedCodes.forEachIndexed { index, code ->
                println("Item ${index + 1}: $code")
            }
            println("=====================================")

            // 2. We print to the Android Logcat (Green text, easier to find)
            Log.d("ENVA_TEST", "Successfully saved ${scannedCodes.size} codes: $scannedCodes")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ProduccionNuevaScreenPreview() {
    ProduccionNuevaScreen(onBackClick = {})
}
