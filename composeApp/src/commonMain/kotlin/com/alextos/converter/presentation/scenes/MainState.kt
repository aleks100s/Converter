package com.alextos.converter.presentation.scenes

import com.alextos.converter.domain.models.CurrencyRate

data class MainState(
    val rates: List<CurrencyRate> = emptyList(),
    val topText: String = "1",
    val bottomText: String = "100",
    val topCurrency: CurrencyRate? = null,
    val bottomCurrency: CurrencyRate? = null,
)
