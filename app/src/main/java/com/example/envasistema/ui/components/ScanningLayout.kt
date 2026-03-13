package com.example.envasistema.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

@Composable
fun ScanningLayout(
    title: String,
    subtitle: String,
    infoText: String,
    onBackClick: () -> Unit,
    onSaveClick: (List<String>) -> Unit,
    counterLabel: String = "Códigos escaneados",
    saveButtonText: String = "Guardar Ingreso",
    saveButtonIcon: ImageVector = Icons.Default.Save,
    primaryColor: Color = Color(0xFF0061A6),
    infoCardBackground: Color = Color(0xFFE1F5FE),
    infoIcon: ImageVector = Icons.Default.Radar,
    infoIconColor: Color? = null,
    isSaveButtonEnabled: ((Int) -> Boolean)? = null,
    showScanningArea: Boolean = true,
    showInternalCounter: Boolean = true, // New parameter to allow State Hoisting of the staging area
    extraContent: @Composable (ColumnScope.() -> Unit)? = null,
    externalScannedCodes: List<String>? = null,
    onCodeScanned: ((String) -> Unit)? = null,
    scannedItemsContent: @Composable (ColumnScope.() -> Unit)? = null
) {
    var internalScannedCodes by remember { mutableStateOf(emptyList<String>()) }
    val scannedCodes = externalScannedCodes ?: internalScannedCodes
    
    var currentInput by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    
    val isPreview = LocalInspectionMode.current
    val scanner = remember { 
        if (isPreview) null else GmsBarcodeScanning.getClient(context) 
    }

    val scannCount = scannedCodes.size
    val buttonEnabled = isSaveButtonEnabled?.invoke(scannCount) ?: (scannCount > 0)

    val handleNewCode: (String) -> Unit = { code ->
        if (code.isNotEmpty() && !scannedCodes.contains(code)) {
            if (externalScannedCodes == null) {
                internalScannedCodes = internalScannedCodes + code
            }
            onCodeScanned?.invoke(code)
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .clickable { focusRequester.requestFocus() }
    ) {
        TextField(
            value = currentInput,
            onValueChange = { newValue ->
                if (newValue.contains("\n")) {
                    val code = newValue.replace("\n", "").trim()
                    handleNewCode(code)
                    currentInput = ""
                } else {
                    currentInput = newValue
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .alpha(0f)
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    val code = currentInput.trim()
                    handleNewCode(code)
                    currentInput = ""
                    focusRequester.requestFocus()
                }
            )
        )

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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(1.dp, Color(0xFFBDBDBD), RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { 
                            scanner?.startScan()
                                ?.addOnSuccessListener { barcode ->
                                    val code = barcode.rawValue
                                    if (code != null) {
                                        handleNewCode(code)
                                    }
                                    focusRequester.requestFocus()
                                }
                                ?.addOnCanceledListener {
                                    focusRequester.requestFocus()
                                }
                                ?.addOnFailureListener {
                                    focusRequester.requestFocus()
                                }
                        },
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
                            text = "(Toque aquí para escanear con la cámara)",
                            color = Color(0xFF9E9E9E),
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (showInternalCounter) {
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
                                Text(text = scannedCodes.size.toString(), color = Color.White, fontWeight = FontWeight.Bold)
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

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    if (scannedCodes.isEmpty()) {
                        Text(
                            text = "No hay códigos escaneados",
                            color = Color(0xFFBDBDBD),
                            fontSize = 15.sp
                        )
                    } else {
                        scannedItemsContent?.invoke(this)
                    }
                } else {
                    // If internal counter is disabled, we just render the content
                    // which is expected to include its own header (like ScannedItemsStagingArea)
                    scannedItemsContent?.invoke(this)
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { if (buttonEnabled) onSaveClick(scannedCodes) },
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
