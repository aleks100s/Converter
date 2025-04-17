package com.alextos.converter.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.alextos.converter.domain.models.CurrencyCode

class DatabaseSeeder: RoomDatabase.Callback() {
    override fun onCreate(connection: SQLiteConnection) {
        super.onCreate(connection)
        // Seed the database with initial data
        val currencies = CurrencyCode.entries.joinToString(", ") {
            "('${it.name}', 0, 0, 0)"
        }
        connection.execSQL("INSERT INTO currencies (code, isFavourite, isMain, priority) VALUES $currencies;")
        connection.execSQL("INSERT INTO currency_rates (code, rate) VALUES ('RUB', 1.0);")
    }
}