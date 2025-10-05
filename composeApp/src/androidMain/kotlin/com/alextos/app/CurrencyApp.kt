package com.alextos.app

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.alextos.camera.CameraActivity
import com.alextos.converter.domain.camera.CameraProps
import com.alextos.di.ConverterAppDelegate
import com.alextos.converter.domain.camera.ConverterUseCase
import com.alextos.di.initKoin
import com.alextos.widget.FavouriteCurrencyWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext

class CurrencyApp: Application() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        // Initialize Koin or any other dependency injection framework here
        initKoin(delegate = object : ConverterAppDelegate {
            override val isCameraFeatureAvailable = false

            override fun showCamera(converterUseCase: ConverterUseCase, props: CameraProps) {
                val intent = Intent(this@CurrencyApp, CameraActivity::class.java).apply {
                    setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra("title", props.title)
                    putExtra("button", props.button)
                }
                startActivity(intent)
            }

            override fun updateCurrencies(favourites: List<String>, main: String?) {
                coroutineScope.launch {
                    val glanceManager = GlanceAppWidgetManager(this@CurrencyApp)
                    val glanceIds = glanceManager.getGlanceIds(FavouriteCurrencyWidget::class.java)

                    glanceIds.forEach { glanceId ->
                        FavouriteCurrencyWidget().update(this@CurrencyApp, glanceId)
                    }
                }
            }
        }) {
            androidContext(this@CurrencyApp)
        }
    }
}