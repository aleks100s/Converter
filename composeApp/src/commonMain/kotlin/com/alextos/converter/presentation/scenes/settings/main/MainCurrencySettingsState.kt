package com.alextos.converter.presentation.scenes.settings.main

import com.alextos.converter.domain.models.CurrencyRate

data class MainCurrencySettingsState(
    val currencies: List<CurrencyRate> = emptyList()
)