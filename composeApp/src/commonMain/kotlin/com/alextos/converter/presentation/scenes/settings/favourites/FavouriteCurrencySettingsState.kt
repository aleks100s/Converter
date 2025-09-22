package com.alextos.converter.presentation.scenes.settings.favourites

import com.alextos.converter.domain.models.CurrencyRate

data class FavouriteCurrencySettingsState(
    val currencies: List<CurrencyRate> = emptyList()
)
