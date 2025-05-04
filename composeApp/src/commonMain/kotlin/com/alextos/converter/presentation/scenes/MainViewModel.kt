package com.alextos.converter.presentation.scenes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alextos.common.preciseFormat
import com.alextos.converter.domain.repository.CurrencyRepository
import com.alextos.converter.domain.storage.ConverterState
import com.alextos.converter.domain.storage.StorageService
import com.alextos.converter.domain.camera.ConverterAppDelegate
import com.alextos.converter.domain.camera.ConverterUseCase
import com.alextos.converter.domain.services.ClipboardService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: CurrencyRepository,
    private val storage: StorageService,
    private val delegate: ConverterAppDelegate,
    private val converterUseCase: ConverterUseCase,
    private val clipboardService: ClipboardService
): ViewModel() {
    private val _state = MutableStateFlow(MainState(onboardingState = OnboardingState(
        step = OnboardingStep.Initial,
        refreshButtonAlpha = 0f,
        isNextOnboardingButtonVisible = true,
        swapButtonAlpha = 0f,
        bottomEditorAlpha = 0f,
        hintAlpha = 0f,
    )))
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
                            bottomCurrency = rates.firstOrNull {
                                it.code == (state.bottomCurrency?.code ?: savedState.bottomCurrency)
                            },
                            topCurrency = rates.firstOrNull {
                                it.code == (state.topCurrency?.code ?: savedState.topCurrency)
                            },
                            isLoading = false
                        )
                    }
                    onAction(MainAction.TopTextChanged(state.value.topText))
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchCurrencyRates()
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.collect {
                converterUseCase.setCurrencies(
                    it.topCurrency,
                    it.bottomCurrency
                )
            }
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
            is MainAction.ShowCamera -> {
                delegate.showCamera(converterUseCase, action.props)
            }
            is MainAction.CopyButtonTapped -> {
                clipboardService.copyToClipboard(action.text, action.label)
            }
            is MainAction.NextOnboardingStepButtonTapped -> {
                val old = state.value.onboardingState
                val onboardingState: OnboardingState = when (old.step) {
                    OnboardingStep.Initial -> {
                        old.copy(
                            step = OnboardingStep.TopEditor,
                            topEditorAlpha = 1f,
                            topEditorBackgroundAlpha = 0.1f,
                            isTopEditorTextVisible = true
                        )
                    }
                    OnboardingStep.TopEditor -> {
                        old.copy(
                            step = OnboardingStep.BottomEditor,
                            topEditorAlpha = 0.3f,
                            topEditorBackgroundAlpha = 0f,
                            isTopEditorTextVisible = false,
                            bottomEditorAlpha = 1f,
                            bottomEditorBackgroundAlpha = 0.1f,
                            isBottomEditorTextVisible = true
                        )
                    }
                    OnboardingStep.BottomEditor -> {
                        old.copy(
                            step = OnboardingStep.SwapButton,
                            bottomEditorAlpha = 0.3f,
                            bottomEditorBackgroundAlpha = 0f,
                            isBottomEditorTextVisible = false,
                            swapButtonAlpha = 1f,
                            swapButtonBackgroundAlpha = 0.1f,
                            isSwapButtonTextVisible = true
                        )
                    }
                    OnboardingStep.SwapButton -> {
                        old.copy(
                            step = OnboardingStep.RefreshButton,
                            swapButtonAlpha = 0.3f,
                            swapButtonBackgroundAlpha = 0f,
                            isSwapButtonTextVisible = false,
                            refreshButtonAlpha = 1f,
                            refreshButtonBackgroundAlpha = 0.1f,
                            isRefreshButtonTextVisible = true
                        )
                    }
                    OnboardingStep.RefreshButton -> {
                        old.copy(
                            step = OnboardingStep.CameraButton,
                            refreshButtonAlpha = 0.3f,
                            refreshButtonBackgroundAlpha = 0f,
                            isRefreshButtonTextVisible = false,
                            isCameraButtonTextVisible = true,
                        )
                    }
                    OnboardingStep.CameraButton -> {
                        old.copy(
                            step = OnboardingStep.Done,
                            topEditorAlpha = 1f,
                            bottomEditorAlpha = 1f,
                            swapButtonAlpha = 1f,
                            refreshButtonAlpha = 1f,
                            isCameraButtonTextVisible = false,
                            hintAlpha = 1f,
                            isNextOnboardingButtonVisible = false
                        )
                    }
                    OnboardingStep.Done -> {
                        old
                    }
                }
                _state.update { state ->
                     state.copy(onboardingState = onboardingState)
                }
            }
        }
    }
}