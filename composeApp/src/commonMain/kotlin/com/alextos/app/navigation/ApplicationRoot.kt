package com.alextos.app.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alextos.converter.presentation.scenes.MainScreen
import com.alextos.converter.presentation.scenes.MainViewModel
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun ApplicationRoot() {
    val navController = rememberNavController()
    val focusManager = LocalFocusManager.current

    Scaffold(modifier = Modifier.fillMaxSize()
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            focusManager.clearFocus()
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            navController = navController,
            startDestination = Route.Converter
        ) {
            composable<Route.Converter> {
                val viewModel = remember { getKoin().get<MainViewModel>() }
                MainScreen(viewModel)
            }
        }
    }
}