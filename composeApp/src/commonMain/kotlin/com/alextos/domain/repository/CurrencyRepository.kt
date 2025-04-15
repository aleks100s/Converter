package com.alextos.domain.repository

import com.alextos.domain.models.CurrencyRate

interface CurrencyRepository {
    suspend fun getCurrencyRates(): List<CurrencyRate>
}