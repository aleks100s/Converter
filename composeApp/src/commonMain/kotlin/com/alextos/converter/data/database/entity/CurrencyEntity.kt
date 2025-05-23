package com.alextos.converter.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = false)
    val code: String,
    val sign: String,
    val flag: String,
    val isFavourite: Boolean,
    val isMain: Boolean
)
