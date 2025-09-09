package com.alextos.converter.presentation.scenes.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alextos.converter.domain.models.CurrencyRate
import com.alextos.converter.presentation.extensions.localization
import converter.composeapp.generated.resources.Res
import converter.composeapp.generated.resources.converter_settings
import converter.composeapp.generated.resources.favourite_currency
import converter.composeapp.generated.resources.ic_heart
import converter.composeapp.generated.resources.ic_heart_fill
import converter.composeapp.generated.resources.main_currency
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsSheet(
    viewModel: SettingsViewModel = koinViewModel(),
    dismiss: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.converter_settings),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceBright)
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(stringResource(Res.string.main_currency))
                    Text(stringResource(Res.string.favourite_currency))
                }
            }

            items(state.currencies) { currencyRate ->
                CurrencyItem(
                    currencyRate = currencyRate,
                    onToggleFavourite = { viewModel.onAction(SettingsAction.ToggleFavourite(it)) },
                    onSelectMainCurrency = { viewModel.onAction(SettingsAction.SelectMainCurrency(it)) }
                )
            }
        }

        Button(
            onClick = dismiss
        ) {
            Text("OK")
        }
    }
}

@Composable
private fun CurrencyItem(
    currencyRate: CurrencyRate,
    onToggleFavourite: (CurrencyRate) -> Unit,
    onSelectMainCurrency: (CurrencyRate) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onToggleFavourite(currencyRate) }
        ) {
            Icon(
                imageVector = if (currencyRate.isFavourite) vectorResource(Res.drawable.ic_heart_fill) else vectorResource(Res.drawable.ic_heart),
                contentDescription = null,
                tint = if (currencyRate.isFavourite) Color.Red else Color.Gray,
                modifier = Modifier
            )
        }

        Text(
            text = currencyRate.code.localization.asString(),
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = currencyRate.isMain,
            onCheckedChange = { onSelectMainCurrency(currencyRate) }
        )
    }
}