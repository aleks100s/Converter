package com.alextos.domain.models

import kotlinx.serialization.SerialName

data class CurrencyRate(
    val code: CurrencyCode,
    val rate: Double,
)
