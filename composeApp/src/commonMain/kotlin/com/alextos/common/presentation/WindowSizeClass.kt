package com.alextos.common.presentation

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun windowSizeClassOf(width: Dp, height: Dp): WindowSizeClass =
    WindowSizeClass.calculateFromSize(DpSize(width, height))

@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val windowSize = LocalWindowInfo.current.containerSize
    return with(LocalDensity.current) {
        windowSizeClassOf(windowSize.width.toDp(), windowSize.height.toDp())
    }
}
