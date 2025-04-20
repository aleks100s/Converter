package com.alextos.converter.data

import com.alextos.converter.data.database.dao.CurrencyRateDao
import com.alextos.converter.data.database.entity.CurrencyRateEntity
import com.alextos.converter.data.mappers.toEntity
import com.alextos.converter.data.network.RemoteCurrencyDataSource
import com.alextos.converter.domain.models.CurrencyCode
import com.alextos.converter.domain.models.CurrencyRate
import com.alextos.converter.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter

class CurrencyRepositoryImpl(
    private val remoteDataSource: RemoteCurrencyDataSource,
    private val dao: CurrencyRateDao,
): CurrencyRepository {
    override fun getCurrencyRates(): Flow<List<CurrencyRate>> {
        return combine(
            dao.getCurrencyRates().filter { it.isNotEmpty() },
            dao.getCurrencies()
        ) { rates, currencies ->
            currencies.map { currencyEntity ->
                CurrencyRate(
                    code = CurrencyCode.valueOf(currencyEntity.code),
                    priority = currencyEntity.priority,
                    isMain = currencyEntity.isMain,
                    isFavourite = currencyEntity.isFavourite,
                    rate = rates.firstOrNull { it.code == currencyEntity.code }?.rate ?: 0.0
                )
            }
        }
    }

    override suspend fun fetchCurrencyRates() {
        val response = remoteDataSource.getCurrencyRates().getOrDefault(emptyList())
        val existingCodes = CurrencyCode.entries.map { it.name }
        val entities = response
            .filter { existingCodes.contains(it.charCode) }
            .map { it.toEntity() }
        val rub = CurrencyRateEntity(code = CurrencyCode.RUB.name, rate = 1.0)
        dao.upsertCurrencyRates(entities + listOf(rub))
    }

    override suspend fun increasePriority(currency: CurrencyRate) {
        dao.upsertCurrency(currency = currency.copy(priority = currency.priority + 1).toEntity())
    }
}