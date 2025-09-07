package com.alextos.converter.presentation.scenes.settings

import com.alextos.converter.domain.models.CurrencyRate

data class SettingsState(
    val currencies: List<CurrencyRate> = emptyList()
)
