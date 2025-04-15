package com.alextos.data.network

import com.alextos.core.data.safeCall
import com.alextos.data.network.dto.CurrencyRateDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get

private const val BASE_URL = "https://www.cbr.ru/scripts/XML_daily.asp"

class KtorRemoteCurrencyDataSource(
    private val httpClient: HttpClient,
): RemoteCurrencyDataSource {
    override suspend fun getCurrencyRates(): Result<List<CurrencyRateDto>> {
        // Simulate network call
        return safeCall {
            httpClient.get(
                urlString = BASE_URL
            ) {
                // parameter("data_req", "15/04/2025")
            }
        }
    }
}