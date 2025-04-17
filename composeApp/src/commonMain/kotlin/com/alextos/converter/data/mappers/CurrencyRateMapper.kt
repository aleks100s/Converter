package com.alextos.converter.data.mappers

import com.alextos.converter.data.database.entity.CurrencyRateEntity
import com.alextos.converter.data.network.dto.CurrencyRateDto
import com.alextos.converter.domain.models.CurrencyCode
import com.alextos.converter.domain.models.CurrencyRate

fun CurrencyRateEntity.toDomain(): CurrencyRate {
    return CurrencyRate(
        code = CurrencyCode.valueOf(code),
        rate = rate
    )
}

fun CurrencyRateDto.toEntity(): CurrencyRateEntity {
    return CurrencyRateEntity(
        code = charCode,
        rate = value / nominal
    )
}