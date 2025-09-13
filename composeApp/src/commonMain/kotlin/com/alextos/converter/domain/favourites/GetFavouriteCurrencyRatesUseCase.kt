package com.alextos.converter.domain.favourites

import com.alextos.converter.domain.camera.ConverterAppDelegate
import com.alextos.converter.domain.models.CurrencyRate
import com.alextos.converter.domain.repository.CurrencyRepository
import kotlinx.datetime.Clock

class GetFavouriteCurrencyRatesUseCase(
    private val repository: CurrencyRepository,
    private val appDelegate: ConverterAppDelegate
) {
    data class Result(val currencyRates: List<CurrencyRate>, val timestamp: Long)

    suspend operator fun invoke() {
        repository.downloadCurrencyRates()
        val main = repository.fetchMainCurrencyRate() ?: return
        val favourites = repository.fetchFavouriteCurrencyRates()
        if (favourites.isEmpty()) {
            return
        }
        val result = Result(
            currencyRates = favourites.map { currencyRate ->
                currencyRate.copy(rate = currencyRate.rate / main.rate, sign = main.sign)
            },
            timestamp = Clock.System.now().epochSeconds
        )
        appDelegate.updateFavouriteRates(result)
    }
}