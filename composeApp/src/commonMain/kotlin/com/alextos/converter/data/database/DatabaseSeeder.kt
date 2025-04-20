package com.alextos.converter.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.alextos.converter.domain.models.CurrencyCode
import com.alextos.converter.domain.models.emoji
import com.alextos.converter.domain.models.symbol

class DatabaseSeeder: RoomDatabase.Callback() {
    override fun onCreate(connection: SQLiteConnection) {
        super.onCreate(connection)
        // Seed the database with initial data
        val currencies = CurrencyCode.entries.joinToString(", ") {
            "('${it.name}', '${it.symbol}', '${it.emoji}', 0, 0, 0)"
        }
        connection.execSQL("INSERT INTO currencies (code, sign, flag, isFavourite, isMain, priority) VALUES $currencies;")
    }
}