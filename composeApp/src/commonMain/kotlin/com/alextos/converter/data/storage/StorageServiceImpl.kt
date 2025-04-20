package com.alextos.converter.data.storage

import com.alextos.converter.domain.models.CurrencyCode
import com.alextos.converter.domain.storage.ConverterState
import com.alextos.converter.domain.storage.StorageService
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class StorageServiceImpl(
    private val settings: Settings,
): StorageService {
    override fun saveState(state: ConverterState) {
        settings["topText"] = state.topText
        settings["bottomText"] = state.bottomText
        settings["topCurrency"] = state.topCurrency?.name
        settings["bottomCurrency"] = state.bottomCurrency?.name
    }

    override fun getState(): ConverterState {
        val topText = settings["topText", "0"]
        val bottomText = settings["bottomText", "0"]
        val topCurrency = CurrencyCode.valueOf(settings["topCurrency", "USD"])
        val bottomCurrency = CurrencyCode.valueOf(settings["bottomCurrency", "RUB"])
        return ConverterState(
            topText = topText,
            bottomText = bottomText,
            topCurrency = topCurrency,
            bottomCurrency = bottomCurrency
        )
    }
}