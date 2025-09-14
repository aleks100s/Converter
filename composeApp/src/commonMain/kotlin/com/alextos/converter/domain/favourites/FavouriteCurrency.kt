package com.alextos.converter.domain.favourites

import com.alextos.converter.domain.models.CurrencyCode

data class FavouriteCurrency(
    val currencyCode: CurrencyCode,
    val rate: String
)