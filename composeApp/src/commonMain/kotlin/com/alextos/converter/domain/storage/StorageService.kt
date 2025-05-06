package com.alextos.converter.domain.storage

import com.alextos.converter.domain.models.CurrencyCode

interface StorageService {
    fun saveState(state: ConverterState)
    fun getState(): ConverterState
    fun isOnboardingFinished(): Boolean
    fun finishOnboarding()
    fun isFirstLaunch(): Boolean
}

data class ConverterState(
    val topText: String,
    val bottomText: String,
    val topCurrency: CurrencyCode?,
    val bottomCurrency: CurrencyCode?
)