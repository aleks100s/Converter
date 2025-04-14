package com.alextos.presentation.scenes

import androidx.lifecycle.ViewModel
import com.alextos.domain.services.KtorService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(
    private val ktorService: KtorService
): ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    fun onAction(action: MainAction) {}
}