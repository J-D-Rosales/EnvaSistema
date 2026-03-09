package com.example.envasistema.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    primaryColor: Color = Color(0xFF0061A6),
    iconBackgroundColor: Color = Color(0xFFE1F5FE),
    iconTintColor: Color = Color(0xFF01579B),
    rightIcon: ImageVector? = Icons.AutoMirrored.Filled.KeyboardArrowRight,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Accent Strip
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(6.dp)
                    .background(primaryColor)
            )

            // Icon Background Section
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(85.dp)
                    .background(iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTintColor,
                    modifier = Modifier.size(34.dp)
                )
            }

            // Text Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    color = if (primaryColor == Color(0xFF0061A6)) Color(0xFF0D47A1) else primaryColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = Color(0xFF757575).copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    lineHeight = 16.sp
                )
            }

            // Right Icon
            if (rightIcon != null) {
                Icon(
                    imageVector = rightIcon,
                    contentDescription = null,
                    tint = if (primaryColor == Color(0xFF0061A6)) Color(0xFFBDBDBD) else primaryColor.copy(alpha = 0.5f),
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }
    }
}
