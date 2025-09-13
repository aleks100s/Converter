package com.alextos.converter.domain.camera

import com.alextos.converter.domain.favourites.GetFavouriteCurrencyRatesUseCase

interface ConverterAppDelegate {
    fun showCamera(converterUseCase: ConverterUseCase, props: CameraProps)
    fun updateFavouriteRates(result: GetFavouriteCurrencyRatesUseCase.Result?)
}