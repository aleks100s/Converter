package com.alextos.converter.presentation.scenes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                    _state.update {
                        it.copy(rates = rates)
                    }
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchCurrencyRates()
        }
    }

    fun onAction(action: MainAction) {}
}