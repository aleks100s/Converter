package com.alextos.converter.data.network

import com.alextos.converter.data.network.dto.CurrencyRateDto

interface RemoteCurrencyDataSource {
    suspend fun getCurrencyRates(): Result<List<CurrencyRateDto>>
}