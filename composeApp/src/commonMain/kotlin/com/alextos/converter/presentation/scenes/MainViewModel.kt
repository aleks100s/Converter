package com.alextos.converter.presentation.scenes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alextos.common.preciseFormat
import com.alextos.converter.domain.models.CurrencyCode
import com.alextos.converter.domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: CurrencyRepository
): ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrencyRates()
                .collect { rates ->
                    _state.update { state ->
                        state.copy(
                            rates = rates,
                            bottomCurrency = state.bottomCurrency ?: rates.firstOrNull { it.code == CurrencyCode.RUB },
                            topCurrency = state.topCurrency ?: rates.firstOrNull { it.code == CurrencyCode.USD },
                        )
                    }
                    onAction(MainAction.TopTextChanged(state.value.topText))
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchCurrencyRates()
        }
    }

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.TopTextChanged -> {
                val topValue = action.text.replace(Regex("[^\\d.,]"), "").replace(",", ".").toDoubleOrNull() ?: 0.0
                val topRubValue = topValue * (state.value.topCurrency?.rate ?: 0.0)
                val bottomRubValue = topRubValue / (state.value.bottomCurrency?.rate ?: 1.0)
                _state.update { state ->
                    state.copy(
                        topText = action.text,
                        bottomText = bottomRubValue.preciseFormat(),
                    )
                }
            }
            is MainAction.BottomTextChanged -> {
                val bottomValue = action.text.replace(Regex("[^\\d.,]"), "").replace(",", ".").toDoubleOrNull() ?: 0.0
                val bottomRubValue = bottomValue * (state.value.bottomCurrency?.rate ?: 0.0)
                val topRubValue = bottomRubValue / (state.value.topCurrency?.rate ?: 1.0)
                _state.update { state ->
                    state.copy(
                        bottomText = action.text,
                        topText = topRubValue.preciseFormat(),
                    )
                }
            }
            is MainAction.SwapCurrencies -> {
                _state.update { state ->
                    state.copy(
                        topCurrency = state.bottomCurrency,
                        bottomCurrency = state.topCurrency
                    )
                }
                onAction(MainAction.TopTextChanged(state.value.topText))
            }
            is MainAction.TopCurrencySelected -> {
                _state.update { state ->
                    state.copy(topCurrency = action.currency)
                }
                onAction(MainAction.TopTextChanged(state.value.topText))
                viewModelScope.launch(Dispatchers.IO) {
                    repository.increasePriority(action.currency)
                }
            }
            is MainAction.BottomCurrencySelected -> {
                _state.update { state ->
                    state.copy(bottomCurrency = action.currency)
                }
                onAction(MainAction.TopTextChanged(state.value.topText))
                viewModelScope.launch(Dispatchers.IO) {
                    repository.increasePriority(action.currency)
                }
            }
        }
    }
}