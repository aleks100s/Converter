package com.alextos.converter.presentation.scenes.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alextos.common.presentation.Screen
import converter.composeapp.generated.resources.Res
import converter.composeapp.generated.resources.converter_settings
import converter.composeapp.generated.resources.favourite_currencies
import converter.composeapp.generated.resources.favourite_currency_info
import converter.composeapp.generated.resources.main_currency
import converter.composeapp.generated.resources.main_currency_info
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    openMainCurrencySettings: () -> Unit,
    openFavouriteCurrencySettings: () -> Unit,
    dismiss: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Screen(
        modifier = Modifier,
        title = stringResource(Res.string.converter_settings),
        goBack = dismiss
    ) {
        LazyColumn(modifier = it.fillMaxWidth()) {
            item {
                SettingsItem(
                    leadingText = stringResource(Res.string.main_currency),
                    trailingText = state.mainCurrencyCode,
                    caption = stringResource(Res.string.main_currency_info),
                    onClick = openMainCurrencySettings
                )
            }

            item {
                SettingsItem(
                    leadingText = stringResource(Res.string.favourite_currencies),
                    caption = stringResource(Res.string.favourite_currency_info),
                    onClick = openFavouriteCurrencySettings
                )
            }
        }
    }
}

@Composable
private fun SettingsItem(
    leadingText: String,
    trailingText: String? = null,
    caption: String,
    onClick: () -> Unit
) {
    Column {
        Column(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = leadingText)

                trailingText?.let {
                    Text(text = it,
                        softWrap = false,
                        overflow = TextOverflow.Visible)
                }
            }

            Text(
                text = caption,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        HorizontalDivider()
    }
}