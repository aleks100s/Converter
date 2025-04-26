package com.alextos.app

import android.app.Application
import com.alextos.di.ConverterAppDelegate
import com.alextos.di.initKoin
import org.koin.android.ext.koin.androidContext

class CurrencyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Koin or any other dependency injection framework here
        initKoin(delegate = object : ConverterAppDelegate {
            override fun showCamera() {

            }
        }) {
            androidContext(this@CurrencyApp)
        }
    }
}