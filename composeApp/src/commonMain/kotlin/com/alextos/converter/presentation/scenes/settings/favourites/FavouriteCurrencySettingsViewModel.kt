package com.alextos.converter.presentation.scenes.settings.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alextos.converter.domain.models.CurrencyRate
import com.alextos.converter.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavouriteCurrencySettingsViewModel(
    private val repository: CurrencyRepository
): ViewModel() {
    private val _state = MutableStateFlow(FavouriteCurrencySettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getCurrencyRates()
                .collect { rates ->
                    _state.update { it.copy(currencies = rates) }
                }
        }
    }

    fun toggleFavouriteCurrency(currencyRate: CurrencyRate) {
        viewModelScope.launch {
            repository.updateCurrency(
                currencyRate.copy(isFavourite = !currencyRate.isFavourite)
            )
        }
    }
}