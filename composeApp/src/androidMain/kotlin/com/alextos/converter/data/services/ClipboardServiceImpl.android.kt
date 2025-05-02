package com.alextos.converter.data.services

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.alextos.converter.domain.services.ClipboardService

actual class ClipboardServiceImpl(
    private val application: Application
): ClipboardService {
    override fun copyToClipboard(text: String, label: String) {
        val clipboard = application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
    }
}