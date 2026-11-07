package com.alextos

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.unit.dp
import com.alextos.common.presentation.windowSizeClassOf
import kotlin.test.Test
import kotlin.test.assertEquals

class WindowSizeClassTest {

    @Test
    fun widthThresholdsMatchMaterialBreakpoints() {
        assertEquals(WindowWidthSizeClass.Compact, windowSizeClassOf(599.dp, 800.dp).widthSizeClass)
        assertEquals(WindowWidthSizeClass.Medium, windowSizeClassOf(600.dp, 800.dp).widthSizeClass)
        assertEquals(WindowWidthSizeClass.Medium, windowSizeClassOf(839.dp, 800.dp).widthSizeClass)
        assertEquals(WindowWidthSizeClass.Expanded, windowSizeClassOf(840.dp, 800.dp).widthSizeClass)
    }

    @Test
    fun heightThresholdsMatchMaterialBreakpoints() {
        assertEquals(WindowHeightSizeClass.Compact, windowSizeClassOf(800.dp, 479.dp).heightSizeClass)
        assertEquals(WindowHeightSizeClass.Medium, windowSizeClassOf(800.dp, 480.dp).heightSizeClass)
        assertEquals(WindowHeightSizeClass.Medium, windowSizeClassOf(800.dp, 899.dp).heightSizeClass)
        assertEquals(WindowHeightSizeClass.Expanded, windowSizeClassOf(800.dp, 900.dp).heightSizeClass)
    }
}
