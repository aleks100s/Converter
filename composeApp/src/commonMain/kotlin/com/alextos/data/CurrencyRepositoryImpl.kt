package com.alextos.data

import com.alextos.data.mappers.toDomain
import com.alextos.data.mappers.toEntity
import com.alextos.data.network.RemoteCurrencyDataSource
import com.alextos.domain.models.CurrencyRate
import com.alextos.domain.repository.CurrencyRepository

class CurrencyRepositoryImpl(
    private val remoteDataSource: RemoteCurrencyDataSource
): CurrencyRepository {
    override suspend fun getCurrencyRates(): List<CurrencyRate> {
        val response = remoteDataSource.getCurrencyRates().getOrDefault(emptyList())
        val entities = response.map { it.toEntity() }
        return entities.map { it.toDomain() }
    }
}