package com.alextos.common.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    title: String,
    color: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true,
    onTap: () -> Unit
) {
    Text(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled) { onTap() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        text = title,
        fontWeight = FontWeight.Medium,
        color = if (enabled) color else MaterialTheme.colorScheme.onSurface,
    )
}