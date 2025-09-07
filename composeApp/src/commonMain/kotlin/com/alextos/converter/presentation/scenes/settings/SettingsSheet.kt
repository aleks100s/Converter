package com.alextos.converter.presentation.scenes.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import converter.composeapp.generated.resources.Res
import converter.composeapp.generated.resources.converter_settings
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsSheet(
    viewModel: SettingsViewModel = koinViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(Res.string.converter_settings))
    }
}