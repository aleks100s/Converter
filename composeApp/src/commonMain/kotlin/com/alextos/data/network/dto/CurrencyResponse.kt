package com.alextos.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ValCurs")
data class CurrencyResponse(
    @SerialName("Date") val date: String,
    @SerialName("name") val name: String,
    @SerialName("Valute") val currencyRates: List<CurrencyRateDto>,
)
