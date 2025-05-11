package com.alextos.converter.domain.camera

import com.alextos.common.preciseFormat
import com.alextos.converter.domain.models.CurrencyRate

class ConverterUseCase {
    private var from: CurrencyRate? = null
    private var to: CurrencyRate? = null

    fun setCurrencies(from: CurrencyRate?, to: CurrencyRate?) {
        this.from = from
        this.to = to
    }

    fun convert(value: String): String? {
        val fromRate = from?.rate ?: 0.0
        val toRate = to?.rate ?: 0.0
        if (fromRate == 0.0 || toRate == 0.0) {
            return null
        }

        val number = value.trim { !it.isDigit() }.toDoubleOrNull()
        if (number == null) {
            return null
        }

        return (number * fromRate / toRate).preciseFormat() + to?.sign
    }
}