package com.alextos.converter.data.network

import com.alextos.core.data.safeCall
import com.alextos.converter.data.network.dto.CurrencyRateDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType

private const val BASE_URL = "https://www.cbr.ru/scripts/XML_daily.asp"

class KtorRemoteCurrencyDataSource(
    private val httpClient: HttpClient,
): RemoteCurrencyDataSource {
    override suspend fun getCurrencyRates(): Result<List<CurrencyRateDto>> {
        return safeCall {
            httpClient.get(
                urlString = BASE_URL
            ) {
                contentType(ContentType.Application.Xml)
                // parameter("data_req", "15/04/2025")
            }
        }
    }
}