package com.alextos.converter.domain.repository

import com.alextos.converter.domain.models.CurrencyCode
import com.alextos.converter.domain.models.CurrencyRate
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun getCurrencyRates(): Flow<List<CurrencyRate>>
    suspend fun getCurrencyRatesOnce(): List<Pair<CurrencyCode, Double>>
    suspend fun downloadCurrencyRates()
    suspend fun updateCurrency(currency: CurrencyRate)
    suspend fun updateCurrencies(currencies: List<CurrencyRate>)
}