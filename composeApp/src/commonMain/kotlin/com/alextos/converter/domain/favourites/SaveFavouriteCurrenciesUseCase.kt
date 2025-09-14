package com.alextos.converter.domain.favourites

import com.alextos.converter.domain.models.CurrencyRate
import com.alextos.di.ConverterAppDelegate

class SaveFavouriteCurrenciesUseCase(
    private val appDelegate: ConverterAppDelegate
) {
    operator fun invoke(rates: List<CurrencyRate>) {
        val main = rates.firstOrNull { it.isMain }
        val favourites = rates.filter { it.isFavourite }
        appDelegate.updateCurrencies(favourites.map { it.code.name }, main?.code?.name)
    }
}