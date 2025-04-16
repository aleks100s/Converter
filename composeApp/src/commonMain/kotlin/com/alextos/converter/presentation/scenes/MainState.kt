package com.alextos.converter.presentation.scenes

import com.alextos.converter.domain.models.CurrencyRate

data class MainState(
    val rates: List<CurrencyRate> = emptyList()
)
