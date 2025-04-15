package com.alextos.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Valute")
data class CurrencyRateDto(
    @SerialName("ID") val id: String,
    @SerialName("NumCode") val numCode: String,
    @SerialName("CharCode") val charCode: String,
    @SerialName("Nominal") val nominal: Int,
    @SerialName("Name") val name: String,
    @SerialName("Value") val value: Double,
    @SerialName("VunitRate") val unitRate: Double,
)
