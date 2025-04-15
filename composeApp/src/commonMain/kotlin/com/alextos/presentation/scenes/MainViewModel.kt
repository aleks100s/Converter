package com.alextos.presentation.scenes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alextos.domain.repository.CurrencyRepository
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
            val rates = repository.getCurrencyRates()
            _state.update { currentState ->
                currentState.copy(
                    title = rates.toString()
                )
            }
        }
    }

    fun onAction(action: MainAction) {}
}