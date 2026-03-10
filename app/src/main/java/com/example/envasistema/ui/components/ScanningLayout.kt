package com.example.envasistema.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScanningLayout(
    title: String,
    subtitle: String,
    infoText: String,
    onBackClick: () -> Unit,
    onSaveClick: (Int) -> Unit,
    counterLabel: String = "Códigos escaneados",
    saveButtonText: String = "Guardar Ingreso",
    saveButtonIcon: ImageVector = Icons.Default.Save,
    primaryColor: Color = Color(0xFF0061A6),
    infoCardBackground: Color = Color(0xFFE1F5FE),
    infoIcon: ImageVector = Icons.Default.Radar,
    infoIconColor: Color? = null,
    isSaveButtonEnabled: ((Int) -> Boolean)? = null,
    showScanningArea: Boolean = true,
    extraContent: @Composable (ColumnScope.() -> Unit)? = null
) {
    var scannCount by remember { mutableIntStateOf(0) }
    val buttonEnabled = isSaveButtonEnabled?.invoke(scannCount) ?: (scannCount > 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        SecondaryHeader(
            title = title,
            subtitle = subtitle,
            backgroundColor = primaryColor,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            extraContent?.invoke(this)

            if (showScanningArea) {
                if (extraContent != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Info Alert Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = infoCardBackground,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.width(6.dp).height(60.dp).background(primaryColor))
                        
                        Icon(
                            imageVector = infoIcon,
                            contentDescription = null,
                            tint = infoIconColor ?: primaryColor,
                            modifier = Modifier.padding(horizontal = 12.dp).size(24.dp)
                        )
                        Text(
                            text = infoText,
                            color = if (primaryColor == Color(0xFFB71C1C) || primaryColor == Color(0xFF455A64)) Color(0xFF0061A6) else primaryColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(end = 16.dp, top = 8.dp, bottom = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Scan Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(1.dp, Color(0xFFBDBDBD), RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { scannCount++ },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            color = Color(0xFFEEEEEE),
                            shape = CircleShape,
                            modifier = Modifier.size(80.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null,
                                tint = Color(0xFF9E9E9E),
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Presione el botón lateral del terminal para escanear",
                            color = Color(0xFF607D8B),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "(Toque aquí para simular escaneo)",
                            color = Color(0xFF9E9E9E),
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Scanned Codes Counter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = primaryColor,
                        shape = CircleShape,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = scannCount.toString(), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = counterLabel,
                        color = Color(0xFF455A64),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
                
                if (scannCount == 0) {
                    Text(
                        text = "No hay códigos escaneados",
                        color = Color(0xFFBDBDBD),
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(24.dp))

            // Footer Button
            Button(
                onClick = { if (buttonEnabled) onSaveClick(scannCount) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (buttonEnabled) primaryColor else Color(0xFFB0BEC5),
                    contentColor = Color.White
                )
            ) {
                Icon(imageVector = saveButtonIcon, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = saveButtonText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
