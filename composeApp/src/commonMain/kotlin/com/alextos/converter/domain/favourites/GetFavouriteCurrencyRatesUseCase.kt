package com.alextos.converter.domain.favourites

import com.alextos.converter.domain.models.CurrencyRate
import com.alextos.converter.domain.repository.CurrencyRepository

class GetFavouriteCurrencyRatesUseCase(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(): List<CurrencyRate> {
        return repository.fetchFavouriteCurrencyRates()
    }
}