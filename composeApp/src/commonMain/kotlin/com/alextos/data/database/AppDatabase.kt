package com.alextos.data.database

import androidx.room.RoomDatabase
import com.alextos.data.database.dao.CurrencyRateDao

abstract class AppDatabase: RoomDatabase() {
    abstract fun currencyRateDao(): CurrencyRateDao

    companion object {
        const val DB_NAME = "currency-database"
    }
}