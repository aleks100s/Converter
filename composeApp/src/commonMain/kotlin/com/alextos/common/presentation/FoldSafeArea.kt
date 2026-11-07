package com.alextos.common.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun FoldSafeArea(content: @Composable () -> Unit) {
    val hinges = currentWindowAdaptiveInfo().windowPosture.hingeList
        .filter { it.isSeparating || it.isOccluding }
    var bounds by remember { mutableStateOf<Rect?>(null) }
    val density = LocalDensity.current

    Box(Modifier.fillMaxSize().onGloballyPositioned { bounds = it.boundsInWindow() }) {
        val area = bounds
        if (hinges.isEmpty() || area != null) {
            var safeArea = area
            if (safeArea != null) {
                val gap = with(density) { 8.dp.toPx() }
                for (hinge in hinges) {
                    val fold = hinge.bounds
                    val current = safeArea ?: break
                    if (hinge.isVertical && fold.left >= current.left && fold.right <= current.right &&
                        fold.bottom > current.top && fold.top < current.bottom
                    ) {
                        val left = max(current.left, fold.left - gap)
                        val right = min(current.right, fold.right + gap)
                        safeArea = if (left - current.left >= current.right - right) {
                            Rect(current.left, current.top, left, current.bottom)
                        } else {
                            Rect(right, current.top, current.right, current.bottom)
                        }
                    } else if (!hinge.isVertical && fold.top >= current.top && fold.bottom <= current.bottom &&
                        fold.right > current.left && fold.left < current.right
                    ) {
                        val top = max(current.top, fold.top - gap)
                        val bottom = min(current.bottom, fold.bottom + gap)
                        safeArea = if (top - current.top >= current.bottom - bottom) {
                            Rect(current.left, current.top, current.right, top)
                        } else {
                            Rect(current.left, bottom, current.right, current.bottom)
                        }
                    }
                }
            }
            val padding = if (area != null && safeArea != null) {
                with(density) {
                    Modifier.absolutePadding(
                        left = (safeArea.left - area.left).toDp(),
                        top = (safeArea.top - area.top).toDp(),
                        right = (area.right - safeArea.right).toDp(),
                        bottom = (area.bottom - safeArea.bottom).toDp()
                    )
                }
            } else Modifier
            Box(Modifier.fillMaxSize().then(padding)) { content() }
        }
    }
}
