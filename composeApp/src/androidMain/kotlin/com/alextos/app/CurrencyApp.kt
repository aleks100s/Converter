package com.alextos.app

import android.app.Application
import com.alextos.converter.domain.camera.CameraProps
import com.alextos.converter.domain.camera.ConverterAppDelegate
import com.alextos.converter.domain.camera.ConverterUseCase
import com.alextos.di.initKoin
import org.koin.android.ext.koin.androidContext

class CurrencyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Koin or any other dependency injection framework here
        initKoin(delegate = object : ConverterAppDelegate {
            override fun showCamera(converterUseCase: ConverterUseCase, props: CameraProps) {
                //
            }
        }) {
            androidContext(this@CurrencyApp)
        }
    }
}