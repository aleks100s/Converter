package com.alextos.converter.domain.repository

import com.alextos.converter.domain.models.CurrencyRate
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun getCurrencyRates(): Flow<List<CurrencyRate>>
    suspend fun fetchCurrencyRates()
}