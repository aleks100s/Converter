package com.alextos.converter.presentation.scenes.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alextos.common.presentation.Screen
import com.alextos.converter.presentation.scenes.settings.favourites.FavouriteCurrencySettingsContent
import com.alextos.converter.presentation.scenes.settings.main.MainCurrencySettingsContent
import converter.composeapp.generated.resources.Res
import converter.composeapp.generated.resources.converter_settings
import converter.composeapp.generated.resources.favourite_currencies
import converter.composeapp.generated.resources.favourite_currency_info
import converter.composeapp.generated.resources.main_currency
import converter.composeapp.generated.resources.main_currency_info
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private enum class SettingsDetail { MainCurrency, FavouriteCurrencies }

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    openMainCurrencySettings: () -> Unit,
    openFavouriteCurrencySettings: () -> Unit,
    dismiss: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var selectedDetail by rememberSaveable { mutableStateOf(SettingsDetail.MainCurrency) }
    Screen(
        modifier = Modifier,
        title = stringResource(Res.string.converter_settings),
        goBack = dismiss
    ) { modifier ->
        BoxWithConstraints(modifier = modifier.fillMaxSize()) {
            val isWide = maxWidth >= 600.dp && maxHeight >= 480.dp
            val listWidth = (maxWidth * 0.4f).coerceAtMost(320.dp)
            Row(Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = if (isWide) Modifier.width(listWidth)
                    else Modifier.fillMaxWidth()
                ) {
                    item {
                        SettingsItem(
                            leadingText = stringResource(Res.string.main_currency),
                            trailingText = state.mainCurrencyCode,
                            caption = stringResource(Res.string.main_currency_info),
                            selected = isWide && selectedDetail == SettingsDetail.MainCurrency,
                            onClick = {
                                if (isWide) selectedDetail = SettingsDetail.MainCurrency else openMainCurrencySettings()
                            }
                        )
                    }

                    item {
                        SettingsItem(
                            leadingText = stringResource(Res.string.favourite_currencies),
                            caption = stringResource(Res.string.favourite_currency_info),
                            selected = isWide && selectedDetail == SettingsDetail.FavouriteCurrencies,
                            onClick = {
                                if (isWide) selectedDetail = SettingsDetail.FavouriteCurrencies else openFavouriteCurrencySettings()
                            }
                        )
                    }
                }
                if (isWide) {
                    VerticalDivider()
                    Box(Modifier.weight(1f)) {
                        when (selectedDetail) {
                            SettingsDetail.MainCurrency -> MainCurrencySettingsContent(koinViewModel())
                            SettingsDetail.FavouriteCurrencies -> FavouriteCurrencySettingsContent(koinViewModel())
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsItem(
    leadingText: String,
    trailingText: String? = null,
    caption: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column {
        Column(
            modifier = Modifier
                .background(if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent)
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
