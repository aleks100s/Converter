package com.alextos.converter.data.mappers

import com.alextos.converter.data.database.entity.CurrencyEntity
import com.alextos.converter.data.database.entity.CurrencyRateEntity
import com.alextos.converter.data.network.dto.CurrencyRateDto
import com.alextos.converter.domain.models.CurrencyRate

fun CurrencyRateDto.toEntity(): CurrencyRateEntity {
    return CurrencyRateEntity(
        code = charCode,
        rate = value / nominal
    )
}

fun CurrencyRate.toEntity(): CurrencyEntity {
    return CurrencyEntity(
        code = code.name,
        priority = priority,
        isMain = isMain,
        isFavourite = isFavourite
    )
}