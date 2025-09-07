package com.alextos.converter.presentation.scenes.settings

import com.alextos.converter.domain.models.CurrencyRate

sealed interface SettingsAction {
    data class ToggleFavourite(val currencyRate: CurrencyRate): SettingsAction
    data class SelectMainCurrency(val currencyRate: CurrencyRate): SettingsAction
}