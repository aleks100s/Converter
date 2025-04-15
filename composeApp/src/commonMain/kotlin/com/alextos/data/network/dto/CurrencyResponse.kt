package com.alextos.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("ValCurs")
data class CurrencyResponse(
    @XmlElement(true)
    @SerialName("Date") val date: String,
    @XmlElement(true)
    @SerialName("name") val name: String,
    @XmlElement(true)
    @SerialName("Valute") val currencyRates: List<CurrencyRateDto>,
)
