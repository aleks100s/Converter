package com.alextos.converter.domain.repository

import com.alextos.converter.domain.models.CurrencyRate
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun getCurrencyRates(): Flow<List<CurrencyRate>>
    suspend fun fetchFavouriteCurrencyRates(): List<CurrencyRate>
    suspend fun fetchMainCurrencyRate(): CurrencyRate?
    suspend fun downloadCurrencyRates()
    suspend fun updateCurrency(currency: CurrencyRate)
    suspend fun updateCurrencies(currencies: List<CurrencyRate>)
}