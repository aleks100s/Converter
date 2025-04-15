package com.alextos.data.network.dto

import com.alextos.core.data.DoubleWithCommaSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("Valute")
data class CurrencyRateDto(
    @XmlElement(true)
    @SerialName("ID") val id: String,
    @XmlElement(true)
    @SerialName("NumCode") val numCode: String,
    @XmlElement(true)
    @SerialName("CharCode") val charCode: String,
    @XmlElement(true)
    @SerialName("Nominal") val nominal: Int,
    @XmlElement(true)
    @SerialName("Name") val name: String,
    @XmlElement(true)
    @SerialName("Value") @Serializable(with = DoubleWithCommaSerializer::class) val value: Double,
    @XmlElement(true)
    @SerialName("VunitRate") @Serializable(with = DoubleWithCommaSerializer::class) val unitRate: Double,
)
