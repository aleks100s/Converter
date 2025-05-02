package com.alextos.converter.data.services

import com.alextos.converter.domain.services.ClipboardService
import platform.UIKit.UIPasteboard

actual class ClipboardServiceImpl : ClipboardService {
    override fun copyToClipboard(text: String, label: String) {
        UIPasteboard.generalPasteboard.setString(text)
    }
}