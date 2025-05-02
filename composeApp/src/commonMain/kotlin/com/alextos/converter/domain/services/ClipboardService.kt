package com.alextos.converter.domain.services

import androidx.compose.ui.Modifier

interface ClipboardService {
    fun copyToClipboard(text: String, label: String)
}