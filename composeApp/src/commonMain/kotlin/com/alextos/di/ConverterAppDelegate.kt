package com.alextos.di

import com.alextos.converter.domain.camera.CameraProps
import com.alextos.converter.domain.camera.ConverterUseCase
import com.alextos.converter.domain.storage.ConverterState

interface ConverterAppDelegate {
    val isCameraFeatureAvailable: Boolean
    fun showCamera(converterUseCase: ConverterUseCase, props: CameraProps)
    fun updateCurrencies(favourites: List<String>, main: String?)
    fun lastQuery(state: ConverterState)
}