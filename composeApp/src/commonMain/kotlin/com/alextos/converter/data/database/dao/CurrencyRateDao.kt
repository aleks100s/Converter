package com.alextos.converter.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.alextos.converter.data.database.entity.CurrencyEntity
import com.alextos.converter.data.database.entity.CurrencyRateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyRateDao {
    @Upsert
    suspend fun upsertCurrencyRates(currencyRates: List<CurrencyRateEntity>)

    @Upsert
    suspend fun upsertCurrency(currency: CurrencyEntity)

    @Upsert
    suspend fun upsertCurrencies(currency: List<CurrencyEntity>)

    @Query("SELECT * FROM currencies")
    fun getCurrencies(): Flow<List<CurrencyEntity>>

    @Query("SELECT * FROM currency_rates")
    fun getCurrencyRates(): Flow<List<CurrencyRateEntity>>
}