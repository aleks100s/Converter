package com.alextos.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.provideContent
import com.alextos.converter.domain.favourites.FavouriteCurrency
import com.alextos.converter.domain.favourites.GetFavouriteCurrencyRatesUseCase
import com.alextos.widget.view.FavouriteCurrencyWidgetView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class FavouriteCurrencyWidget: GlanceAppWidget() {
    private val getFavouriteCurrencyRatesUseCase: GetFavouriteCurrencyRatesUseCase by inject(GetFavouriteCurrencyRatesUseCase::class.java)

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val rates = provideRates()
        provideContent {
            GlanceTheme {
                Scaffold {
                    FavouriteCurrencyWidgetView(rates)
                }
            }
        }
    }

    private suspend fun provideRates(): List<FavouriteCurrency> {
        return withContext(Dispatchers.IO) {
            getFavouriteCurrencyRatesUseCase() ?: emptyList()
        }
    }
}