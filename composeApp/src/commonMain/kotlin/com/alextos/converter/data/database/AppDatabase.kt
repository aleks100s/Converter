package com.alextos.converter.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.alextos.converter.data.database.dao.CurrencyRateDao
import com.alextos.converter.data.database.entity.CurrencyEntity
import com.alextos.converter.data.database.entity.CurrencyRateEntity

@Database(
    entities = [
        CurrencyEntity::class,
        CurrencyRateEntity::class,
               ],
    version = 1,
    exportSchema = false
)
@ConstructedBy(DatabaseConstructor::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun currencyRateDao(): CurrencyRateDao

    companion object {
        const val DB_NAME = "currency-database"
    }
}