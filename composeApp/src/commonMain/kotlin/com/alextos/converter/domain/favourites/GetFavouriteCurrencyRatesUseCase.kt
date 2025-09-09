package com.alextos.converter.domain.favourites

import com.alextos.converter.domain.models.CurrencyRate
import com.alextos.converter.domain.repository.CurrencyRepository
import kotlinx.datetime.Clock

class GetFavouriteCurrencyRatesUseCase(
    private val repository: CurrencyRepository
) {
    data class Result(val currencyRates: List<CurrencyRate>, val timestamp: Long)

    suspend operator fun invoke(): Result? {
        repository.downloadCurrencyRates()
        val main = repository.fetchMainCurrencyRate() ?: return null
        val favourites = repository.fetchFavouriteCurrencyRates()
        if (favourites.isEmpty()) {
            return null
        }
        return Result(
            currencyRates = favourites.map { currencyRate ->
                currencyRate.copy(rate = currencyRate.rate / main.rate)
            },
            timestamp = Clock.System.now().epochSeconds
        )
    }
}