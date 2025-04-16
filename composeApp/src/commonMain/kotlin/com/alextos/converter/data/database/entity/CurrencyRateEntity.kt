package com.alextos.converter.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_rates")
data class CurrencyRateEntity(
    @PrimaryKey(autoGenerate = false)
    val code: String,
    val rate: Double,
    val isFavourite: Boolean,
    val isMain: Boolean,
    val timestamp: Long
)
