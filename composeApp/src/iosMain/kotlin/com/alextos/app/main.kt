package com.alextos.app

import androidx.compose.ui.window.ComposeUIViewController
import com.alextos.converter.domain.camera.ConverterAppDelegate
import com.alextos.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(delegate: ConverterAppDelegate): UIViewController = ComposeUIViewController(
    configure = {
        initKoin(delegate = delegate)
    }
) { App() }
