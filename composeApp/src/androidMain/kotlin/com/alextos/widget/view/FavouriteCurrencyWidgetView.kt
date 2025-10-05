package com.alextos.widget.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.alextos.converter.domain.favourites.FavouriteCurrency
import com.alextos.converter.domain.models.emoji

@Composable
fun FavouriteCurrencyWidgetView(rates: List<FavouriteCurrency>) {
    Column {
        rates.chunked(2).forEach { pair ->
            Row(
                modifier = GlanceModifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                pair.forEach { value ->
                    CurrencyRateView(value)
                }

                // если элементов меньше 2 — добавляем пустой блок
                if (pair.size == 1) {
                    Spacer(GlanceModifier.defaultWeight())
                }
            }
        }
    }
}

@Composable
private fun CurrencyRateView(favouriteCurrency: FavouriteCurrency) {
    Column(
        modifier = GlanceModifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "${favouriteCurrency.currencyCode.emoji} ${favouriteCurrency.currencyCode.name}",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = GlanceTheme.colors.onSurface
            )
        )

        Text(
            favouriteCurrency.rate,
            style = TextStyle(
                fontSize = 24.sp,
                color = GlanceTheme.colors.onSurface)
        )
    }
}