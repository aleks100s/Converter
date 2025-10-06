package com.alextos.widget.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.layout.Alignment
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
    LazyColumn(
        modifier = GlanceModifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(rates) { value ->
            CurrencyRateView(value)
        }
    }
}

@Composable
private fun CurrencyRateView(favouriteCurrency: FavouriteCurrency) {
    Row(
        modifier = GlanceModifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "${favouriteCurrency.currencyCode.emoji} ${favouriteCurrency.currencyCode.name}",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = GlanceTheme.colors.onSurface
            )
        )

        Spacer(modifier = GlanceModifier.defaultWeight())

        Text(
            favouriteCurrency.rate,
            style = TextStyle(
                fontSize = 24.sp,
                color = GlanceTheme.colors.onSurface
            )
        )
    }
}