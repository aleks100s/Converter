package com.alextos.converter.data

import com.alextos.converter.data.database.dao.CurrencyRateDao
import com.alextos.converter.data.database.entity.CurrencyRateEntity
import com.alextos.converter.data.mappers.toDomain
import com.alextos.converter.data.mappers.toEntity
import com.alextos.converter.data.network.RemoteCurrencyDataSource
import com.alextos.converter.domain.models.CurrencyCode
import com.alextos.converter.domain.models.CurrencyRate
import com.alextos.converter.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrencyRepositoryImpl(
    private val remoteDataSource: RemoteCurrencyDataSource,
    private val dao: CurrencyRateDao
): CurrencyRepository {
    override fun getCurrencyRates(): Flow<List<CurrencyRate>> {
        return dao.getCurrencyRates().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun fetchCurrencyRates() {
        val response = remoteDataSource.getCurrencyRates().getOrDefault(emptyList())
        val existingCodes = CurrencyCode.entries.map { it.name}
        val entities = response
            .filter { existingCodes.contains(it.charCode) }
            .map { it.toEntity() }
        val rub = CurrencyRateEntity(
            code = "RUB",
            rate = 1.0,
            isFavourite = false,
            isMain = false,
            timestamp = 0L
        )
        dao.upsertCurrencyRates(entities + listOf(rub))
    }
}