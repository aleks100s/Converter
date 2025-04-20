package com.alextos.converter.presentation.scenes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alextos.common.preciseFormat
import com.alextos.converter.domain.repository.CurrencyRepository
import com.alextos.converter.domain.storage.ConverterState
import com.alextos.converter.domain.storage.StorageService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: CurrencyRepository,
    private val storage: StorageService
): ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val savedState = storage.getState()
            _state.update { state ->
                state.copy(
                    topText = savedState.topText,
                    bottomText = savedState.bottomText,
                )
            }
            repository.getCurrencyRates()
                .collect { rates ->
                    _state.update { state ->
                        state.copy(
                            rates = rates,
                            bottomCurrency = state.bottomCurrency ?: rates.firstOrNull { it.code == savedState.bottomCurrency },
                            topCurrency = state.topCurrency ?: rates.firstOrNull { it.code == savedState.topCurrency },
                            isLoading = false
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
                val topRubValue = topValue * (state.value.topCurrency?.rate ?: 1.0)
                val bottomRubValue = topRubValue / (state.value.bottomCurrency?.rate ?: 1.0)
                _state.update { state ->
                    state.copy(
                        topText = action.text.trimStart('0').ifEmpty { "0" },
                        bottomText = bottomRubValue.preciseFormat(),
                    )
                }
                viewModelScope.launch(Dispatchers.IO) {
                    storage.saveState(
                        ConverterState(
                            topText = state.value.topText,
                            bottomText = state.value.bottomText,
                            topCurrency = state.value.topCurrency?.code,
                            bottomCurrency = state.value.bottomCurrency?.code
                        )
                    )
                }
            }
            is MainAction.BottomTextChanged -> {
                val bottomValue = action.text.replace(Regex("[^\\d.,]"), "").replace(",", ".").toDoubleOrNull() ?: 0.0
                val bottomRubValue = bottomValue * (state.value.bottomCurrency?.rate ?: 1.0)
                val topRubValue = bottomRubValue / (state.value.topCurrency?.rate ?: 1.0)
                _state.update { state ->
                    state.copy(
                        bottomText = action.text.trimStart('0').ifEmpty { "0" },
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
            }
            is MainAction.BottomCurrencySelected -> {
                _state.update { state ->
                    state.copy(bottomCurrency = action.currency)
                }
                onAction(MainAction.TopTextChanged(state.value.topText))
            }
            is MainAction.ReloadRates -> {
                _state.update { state ->
                    state.copy(isLoading = true)
                }
                viewModelScope.launch(Dispatchers.IO) {
                    repository.fetchCurrencyRates()
                }
            }
        }
    }
}