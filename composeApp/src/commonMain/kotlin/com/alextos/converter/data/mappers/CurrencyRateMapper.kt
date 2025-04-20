package com.alextos.converter.data.mappers

import com.alextos.converter.data.database.entity.CurrencyEntity
import com.alextos.converter.data.database.entity.CurrencyRateEntity
import com.alextos.converter.data.network.dto.CurrencyRateDto
import com.alextos.converter.domain.models.CurrencyRate
import com.alextos.converter.domain.models.emoji
import com.alextos.converter.domain.models.symbol

fun CurrencyRateDto.toEntity(): CurrencyRateEntity {
    return CurrencyRateEntity(
        code = charCode,
        rate = value / nominal
    )
}

fun CurrencyRate.toEntity(): CurrencyEntity {
    return CurrencyEntity(
        code = code.name,
        isMain = isMain,
        isFavourite = isFavourite,
        flag = code.emoji,
        sign = code.symbol
    )
}