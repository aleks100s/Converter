package com.alextos.common.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitViewController
import com.alextos.app.LocalNativeViewFactory

@Composable
actual fun NativeBanner() {
    val factory = LocalNativeViewFactory.current
    UIKitViewController(
        modifier = Modifier
            .fillMaxWidth()
            .height(134.dp),
        factory = {
            factory.createBannerView()
        }
    )
}