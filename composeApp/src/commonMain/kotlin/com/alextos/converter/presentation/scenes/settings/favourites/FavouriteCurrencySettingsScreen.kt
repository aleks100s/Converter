package com.alextos.converter.presentation.scenes.settings.favourites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alextos.common.presentation.Screen
import com.alextos.converter.domain.models.CurrencyRate
import com.alextos.converter.presentation.extensions.localization
import converter.composeapp.generated.resources.Res
import converter.composeapp.generated.resources.favourite_currencies
import converter.composeapp.generated.resources.favourite_currency_info
import converter.composeapp.generated.resources.ic_heart
import converter.composeapp.generated.resources.ic_heart_fill
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun FavouriteCurrencySettingsScreen(
    viewModel: FavouriteCurrencySettingsViewModel,
    onDismiss: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Screen(
        modifier = Modifier,
        title = stringResource(Res.string.favourite_currencies),
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
                    Text(stringResource(Res.string.favourite_currency_info))
                }
            }

            items(state.currencies) { currencyRate ->
                CurrencyItem(
                    currencyRate = currencyRate,
                    onToggleFavourite = { viewModel.toggleFavouriteCurrency(it) }
                )
            }
        }
    }
}

@Composable
private fun CurrencyItem(
    currencyRate: CurrencyRate,
    onToggleFavourite: (CurrencyRate) -> Unit
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
    }
}