package com.alextos.converter.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alextos.converter.data.database.AppDatabase

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<AppDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(AppDatabase.DB_NAME)
        return Room.databaseBuilder(context = context, name = dbFile.absolutePath)
    }
}