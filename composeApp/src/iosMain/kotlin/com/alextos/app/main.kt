package com.alextos.app

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.ComposeUIViewController
import com.alextos.common.NativeViewFactory
import com.alextos.converter.domain.camera.ConverterAppDelegate
import com.alextos.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(
    delegate: ConverterAppDelegate,
    nativeViewFactory: NativeViewFactory
): UIViewController = ComposeUIViewController(
    configure = {
        initKoin(delegate = delegate)
    }
) {
    CompositionLocalProvider(LocalNativeViewFactory provides nativeViewFactory) {
        App()
    }
}

val LocalNativeViewFactory = staticCompositionLocalOf<NativeViewFactory> {
    error("NativeViewFactory not provided")
}