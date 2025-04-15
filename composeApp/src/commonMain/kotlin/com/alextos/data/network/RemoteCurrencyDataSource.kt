package com.alextos.data.network

import com.alextos.data.network.dto.CurrencyRateDto

interface RemoteCurrencyDataSource {
    suspend fun getCurrencyRates(): Result<List<CurrencyRateDto>>
}