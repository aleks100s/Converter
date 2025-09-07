package com.alextos.converter.presentation.scenes.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alextos.converter.domain.models.CurrencyRate
import com.alextos.converter.domain.repository.CurrencyRepository
import com.alextos.converter.presentation.extensions.localization
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: CurrencyRepository
): ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getCurrencyRates()
                .collect { rates ->
                    _state.update {
                        it.copy(currencies = rates)
                    }
                }
        }
    }

    fun onAction(action: SettingsAction) {
        when(action) {
            is SettingsAction.ToggleFavourite -> toggleFavourite(action.currencyRate)
            is SettingsAction.SelectMainCurrency -> selectMainCurrency(action.currencyRate)
        }
    }

    private fun toggleFavourite(currencyRate: CurrencyRate) {
        viewModelScope.launch {
            repository.updateCurrency(
                currencyRate.copy(isFavourite = !currencyRate.isFavourite)
            )
        }
    }

    private fun selectMainCurrency(currencyRate: CurrencyRate) {
        viewModelScope.launch {
            val allCurrencies = state.value.currencies.map { it.copy(isMain = currencyRate.code == it.code) }
            repository.updateCurrencies(allCurrencies)
        }
    }
}