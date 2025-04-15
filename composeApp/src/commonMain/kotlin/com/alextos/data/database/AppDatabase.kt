package com.alextos.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alextos.data.database.dao.CurrencyRateDao
import com.alextos.data.database.entity.CurrencyRateEntity

@Database(
    entities = [CurrencyRateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun currencyRateDao(): CurrencyRateDao

    companion object {
        const val DB_NAME = "currency-database"
    }
}