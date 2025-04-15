package com.alextos.data

import com.alextos.data.database.dao.CurrencyRateDao
import com.alextos.data.database.entity.CurrencyRateEntity
import com.alextos.data.mappers.toDomain
import com.alextos.data.mappers.toEntity
import com.alextos.data.network.RemoteCurrencyDataSource
import com.alextos.domain.models.CurrencyRate
import com.alextos.domain.repository.CurrencyRepository

class CurrencyRepositoryImpl(
    private val remoteDataSource: RemoteCurrencyDataSource,
    private val dao: CurrencyRateDao
): CurrencyRepository {
    override suspend fun getCurrencyRates(): List<CurrencyRate> {
        val response = remoteDataSource.getCurrencyRates().getOrDefault(emptyList())
        val entities = response.map { it.toEntity() }
        dao.upsertCurrencyRates(listOf(CurrencyRateEntity(code = "RUB", rate = 1.0, isFavourite = false, isMain = true, timestamp = 0L)))
        return entities.map { it.toDomain() }
    }
}