package com.alextos.converter.domain.storage

import com.alextos.converter.domain.models.CurrencyCode
import com.alextos.converter.domain.models.CurrencyRate

interface StorageService {
    fun saveState(state: ConverterState)
    fun getState(): ConverterState
}

data class ConverterState(
    val topText: String,
    val bottomText: String,
    val topCurrency: CurrencyCode?,
    val bottomCurrency: CurrencyCode?
)