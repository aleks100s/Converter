package com.alextos.app

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.alextos.app.navigation.ApplicationRoot
import com.alextos.app.navigation.ApplicationViewModel
import converter.composeapp.generated.resources.*
import com.alextos.app.theme.AppTheme
import com.alextos.app.theme.LocalThemeIsDark
import kotlinx.coroutines.isActive
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.mp.KoinPlatform.getKoin

@Composable
internal fun App() = AppTheme {
    val viewModel = remember { getKoin().get<ApplicationViewModel>() }
    ApplicationRoot(viewModel)
}