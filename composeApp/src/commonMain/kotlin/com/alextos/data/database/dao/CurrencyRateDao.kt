package com.alextos.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.alextos.data.database.entity.CurrencyRateEntity

@Dao
interface CurrencyRateDao {
    @Upsert
    suspend fun upsertCurrencyRates(currencyRates: List<CurrencyRateEntity>)

    @Query("SELECT * FROM currency_rates")
    suspend fun getCurrencyRates(): List<CurrencyRateEntity>
}