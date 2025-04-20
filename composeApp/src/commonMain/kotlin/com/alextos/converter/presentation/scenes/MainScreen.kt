package com.alextos.converter.presentation.scenes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alextos.converter.domain.models.CurrencyRate
import com.alextos.converter.presentation.extensions.emoji
import com.alextos.common.presentation.PickerDropdown
import com.alextos.common.presentation.Screen
import converter.composeapp.generated.resources.Res
import converter.composeapp.generated.resources.converter_reload
import converter.composeapp.generated.resources.converter_swap
import converter.composeapp.generated.resources.converter_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Screen(
        modifier = Modifier,
        title = stringResource(Res.string.converter_title),
        actions = {
            IconButton(
                onClick = {
                    viewModel.onAction(MainAction.ReloadRates)
                }
            ) {
                Icon(
                    Icons.Default.Refresh,
                    stringResource(Res.string.converter_reload),
                )
            }
        }
    ) { modifier ->
        if (state.isLoading) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CurrencyEditor(
                    currency = state.topCurrency,
                    value = state.topText,
                    onValueChanged = { text ->
                        viewModel.onAction(MainAction.TopTextChanged(text))
                    },
                    currencies = state.rates,
                    onCurrencySelected = { currency ->
                        viewModel.onAction(MainAction.TopCurrencySelected(currency))
                    }
                )

                IconButton(
                    onClick = {
                        viewModel.onAction(MainAction.SwapCurrencies)
                    }
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        stringResource(Res.string.converter_swap),
                    )
                }

                CurrencyEditor(
                    currency = state.bottomCurrency,
                    value = state.bottomText,
                    onValueChanged = { text ->
                        viewModel.onAction(MainAction.BottomTextChanged(text))
                    },
                    currencies = state.rates,
                    onCurrencySelected = { currency ->
                        viewModel.onAction(MainAction.BottomCurrencySelected(currency))
                    }
                )
            }
        }
    }
}

@Composable
fun CurrencyEditor(
    currency: CurrencyRate?,
    value: String,
    onValueChanged: (String) -> Unit,
    currencies: List<CurrencyRate>,
    onCurrencySelected: (CurrencyRate) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PickerDropdown(
            selected = currency,
            options = currencies,
            onSelect = onCurrencySelected
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            leadingIcon = {
                Text(text = currency?.code?.emoji ?: "")
            }
        )
    }
}