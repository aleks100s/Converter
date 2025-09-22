package com.alextos.converter.presentation.scenes.settings.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alextos.converter.domain.models.CurrencyRate
import com.alextos.converter.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainCurrencySettingsViewModel(
    private val repository: CurrencyRepository
): ViewModel() {
    private val _state = MutableStateFlow(MainCurrencySettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getCurrencyRates()
                .collect { rates ->
                    _state.update { it.copy(currencies = rates) }
                }
        }
    }

    fun selectMainCurrency(currencyRate: CurrencyRate) {
        viewModelScope.launch {
            val allCurrencies = state.value.currencies.map { it.copy(isMain = currencyRate.code == it.code) }
            repository.updateCurrencies(allCurrencies)
        }
    }
}