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
                    isMain = currencyEntity.isMain,
                    isFavourite = currencyEntity.isFavourite,
                    rate = rates.firstOrNull { it.code == currencyEntity.code }?.rate ?: 0.0,
                    flag = currencyEntity.flag,
                    sign = currencyEntity.sign
                )
            }.sortedBy { it.code }
        }
    }

    override suspend fun fetchFavouriteCurrencyRates(): List<CurrencyRate> {
        val currencies = dao.getFavouriteCurrenciesOnce()
        val rates = dao.getCurrencyRatesOnce()
        return currencies.map { currencyEntity ->
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

    override suspend fun fetchMainCurrencyRate(): CurrencyRate? {
        val currency = dao.getMainCurrenciesOnce().firstOrNull() ?: return null
        val rate = dao.getCurrencyRate(currency.code).firstOrNull() ?: return null
        return CurrencyRate(
            code = CurrencyCode.valueOf(currency.code),
            isMain = currency.isMain,
            isFavourite = currency.isFavourite,
            rate = rate.rate,
            flag = currency.flag,
            sign = currency.sign
        )
    }

    override suspend fun downloadCurrencyRates() {
        val response = remoteDataSource.getCurrencyRates().getOrThrow()
        val existingCodes = CurrencyCode.entries.map { it.name }
        val entities = response
            .filter { existingCodes.contains(it.charCode) }
            .map { it.toEntity() }
        val rub = CurrencyRateEntity(code = CurrencyCode.RUB.name, rate = 1.0)
        dao.upsertCurrencyRates(entities + listOf(rub))
    }

    override suspend fun updateCurrency(currency: CurrencyRate) {
        dao.upsertCurrency(currency.toEntity())
    }

    override suspend fun updateCurrencies(currencies: List<CurrencyRate>) {
        dao.upsertCurrencies(currencies.map { it.toEntity() })
    }
}