package com.alextos.app

import android.app.Application
import android.content.Intent
import com.alextos.camera.CameraActivity
import com.alextos.converter.domain.camera.CameraProps
import com.alextos.di.ConverterAppDelegate
import com.alextos.converter.domain.camera.ConverterUseCase
import com.alextos.di.initKoin
import org.koin.android.ext.koin.androidContext

class CurrencyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Koin or any other dependency injection framework here
        initKoin(delegate = object : ConverterAppDelegate {
            override fun showCamera(converterUseCase: ConverterUseCase, props: CameraProps) {
                val intent = Intent(this@CurrencyApp, CameraActivity::class.java).apply {
                    setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra("title", props.title)
                    putExtra("button", props.button)
                }
                startActivity(intent)
            }

            override fun updateCurrencies(favourites: List<String>, main: String?) {

            }
        }) {
            androidContext(this@CurrencyApp)
        }
    }
}