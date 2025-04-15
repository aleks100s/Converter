package com.alextos.data.mappers

import com.alextos.data.database.entity.CurrencyRateEntity
import com.alextos.data.network.dto.CurrencyRateDto
import com.alextos.domain.models.CurrencyCode
import com.alextos.domain.models.CurrencyRate
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
        code = numCode,
        rate = value,
        isFavourite = false,
        isMain = false,
        timestamp = Clock.System.now().toEpochMilliseconds()
    )
}