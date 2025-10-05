package com.alextos.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.provideContent
import com.alextos.widget.view.FavouriteCurrencyWidgetView

class FavouriteCurrencyWidget: GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            GlanceTheme {
                Scaffold {
                    FavouriteCurrencyWidgetView()
                }
            }
        }
    }
}