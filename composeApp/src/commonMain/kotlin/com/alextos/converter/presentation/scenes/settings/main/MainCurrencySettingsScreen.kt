package com.alextos.converter.presentation.scenes.settings.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alextos.common.presentation.Screen
import com.alextos.converter.domain.models.CurrencyRate
import com.alextos.converter.presentation.extensions.localization
import converter.composeapp.generated.resources.Res
import converter.composeapp.generated.resources.main_currency
import converter.composeapp.generated.resources.main_currency_info
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainCurrencySettingsScreen(
    viewModel: MainCurrencySettingsViewModel,
    onDismiss: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Screen(
        modifier = Modifier,
        title = stringResource(Res.string.main_currency),
        goBack = onDismiss
    ) {
        LazyColumn(
            modifier = it
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            item {
                Column(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceBright)
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(stringResource(Res.string.main_currency_info))
                }
            }

            items(state.currencies) { currencyRate ->
                CurrencyItem(
                    currencyRate = currencyRate,
                    onSelectMainCurrency = { viewModel.selectMainCurrency(it) }
                )
            }
        }
    }
}

@Composable
private fun CurrencyItem(
    currencyRate: CurrencyRate,
    onSelectMainCurrency: (CurrencyRate) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currencyRate.code.localization.asString(),
            modifier = Modifier.weight(1f)
        )

        RadioButton(
            selected = currencyRate.isMain,
            onClick = { onSelectMainCurrency(currencyRate) }
        )
    }
}