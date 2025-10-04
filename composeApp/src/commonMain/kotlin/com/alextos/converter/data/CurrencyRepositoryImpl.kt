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
import kotlinx.coroutines.flow.map

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
                    isMain = currencyEntity.isMain,
                    isFavourite = currencyEntity.isFavourite,
                    rate = rates.firstOrNull { it.code == currencyEntity.code }?.rate ?: 0.0,
                    flag = currencyEntity.flag,
                    sign = currencyEntity.sign
                )
            }.sortedBy { it.code }
        }
    }

    override suspend fun getCurrencyRatesOnce(): List<Pair<CurrencyCode, Double>> {
        return fetchCurrencyRates().map { CurrencyCode.valueOf(it.code) to it.rate }
    }

    override suspend fun downloadCurrencyRates() {
        dao.upsertCurrencyRates(fetchCurrencyRates())
    }

    override suspend fun updateCurrency(currency: CurrencyRate) {
        dao.upsertCurrency(currency.toEntity())
    }

    override suspend fun updateCurrencies(currencies: List<CurrencyRate>) {
        dao.upsertCurrencies(currencies.map { it.toEntity() })
    }

    override suspend fun setMainCurrency(currency: CurrencyCode) {
        dao.setMainCurrency(currency.name)
    }

    private suspend fun fetchCurrencyRates(): List<CurrencyRateEntity> {
        val response = remoteDataSource.getCurrencyRates().getOrThrow()
        val existingCodes = CurrencyCode.entries.map { it.name }
        val entities = response
            .filter { existingCodes.contains(it.charCode) }
            .map { it.toEntity() }
        val rub = CurrencyRateEntity(code = CurrencyCode.RUB.name, rate = 1.0)
        return entities + rub
    }
}