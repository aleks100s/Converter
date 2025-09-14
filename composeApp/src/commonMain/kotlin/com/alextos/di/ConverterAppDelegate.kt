package com.alextos.di

import com.alextos.converter.domain.camera.CameraProps
import com.alextos.converter.domain.camera.ConverterUseCase

interface ConverterAppDelegate {
    fun showCamera(converterUseCase: ConverterUseCase, props: CameraProps)
    fun updateCurrencies(favourites: List<String>, main: String?)
}