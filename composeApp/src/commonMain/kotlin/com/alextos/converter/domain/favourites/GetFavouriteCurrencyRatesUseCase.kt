package com.alextos.converter.domain.favourites

import com.alextos.common.preciseFormat
import com.alextos.converter.domain.models.symbol
import com.alextos.converter.domain.repository.CurrencyRepository

class GetFavouriteCurrencyRatesUseCase(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(favourites: List<String>, main: String?): List<FavouriteCurrency>? {
        if (favourites.isEmpty() || main.isNullOrEmpty()) return null

        val rates = repository.getCurrencyRatesOnce()
        val main = rates.firstOrNull { it.first.name == main } ?: return null
        val favourites = rates.filter { favourites.contains(it.first.name) && it.first != main.first }
        return favourites.map { pair ->
            FavouriteCurrency(
                currencyCode = pair.first,
                rate = (pair.second / main.second).preciseFormat() + " " + main.first.symbol
            )
        }
    }
}