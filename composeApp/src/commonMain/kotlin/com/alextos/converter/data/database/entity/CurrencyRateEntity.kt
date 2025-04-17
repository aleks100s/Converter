package com.alextos.converter.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "currency_rates",
    foreignKeys = [
        ForeignKey(
            entity = CurrencyEntity::class,
            parentColumns = ["code"],
            childColumns = ["code"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("code")]
)
data class CurrencyRateEntity(
    @PrimaryKey(autoGenerate = false)
    val code: String,
    val rate: Double
)
