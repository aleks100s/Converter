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

    @Query("SELECT * FROM currencies")
    suspend fun getCurrencies(): List<CurrencyEntity>

    @Query("SELECT * FROM currency_rates")
    fun getCurrencyRates(): Flow<List<CurrencyRateEntity>>
}