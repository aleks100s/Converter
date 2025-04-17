package com.alextos.converter.data.mappers

import com.alextos.converter.data.database.entity.CurrencyRateEntity
import com.alextos.converter.data.network.dto.CurrencyRateDto
import com.alextos.converter.domain.models.CurrencyCode
import com.alextos.converter.domain.models.CurrencyRate
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

fun CurrencyRateEntity.toDomain(): CurrencyRate {
    return CurrencyRate(
        code = CurrencyCode.valueOf(code),
        rate = rate
    )
}

@OptIn(ExperimentalTime::class)
fun CurrencyRateDto.toEntity(): CurrencyRateEntity {
    return CurrencyRateEntity(
        code = charCode,
        rate = value / nominal,
        isFavourite = false,
        isMain = false,
        timestamp = Clock.System.now().toEpochMilliseconds()
    )
}