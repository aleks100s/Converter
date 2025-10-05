package com.alextos.widget

import android.content.Context
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
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

    companion object {
        private val SMALL_SQUARE = DpSize(100.dp, 100.dp)
        private val HORIZONTAL_RECTANGLE = DpSize(250.dp, 100.dp)
        private val BIG_SQUARE = DpSize(250.dp, 250.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        setOf(
            SMALL_SQUARE,
            HORIZONTAL_RECTANGLE,
            BIG_SQUARE
        )
    )

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