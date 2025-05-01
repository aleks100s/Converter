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

    fun convert(value: Double): String {
        val fromRate = from?.rate ?: 0.0
        val toRate = to?.rate ?: 0.0
        val result = if (from?.code == to?.code) {
            value
        } else if (fromRate > 0 && toRate > 0) {
            value * fromRate / toRate
        } else if (fromRate > 0) {
            value * fromRate
        } else if (toRate > 0) {
            value / toRate
        } else {
            0.0
        }
        return result.preciseFormat() + to?.sign
    }
}