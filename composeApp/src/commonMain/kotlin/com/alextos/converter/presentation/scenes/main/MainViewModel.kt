package com.alextos.converter.presentation.scenes.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alextos.common.UiText
import com.alextos.common.preciseFormat
import com.alextos.converter.domain.repository.CurrencyRepository
import com.alextos.converter.domain.storage.ConverterState
import com.alextos.converter.domain.storage.StorageService
import com.alextos.di.ConverterAppDelegate
import com.alextos.converter.domain.camera.ConverterUseCase
import com.alextos.converter.domain.favourites.SaveFavouriteCurrenciesUseCase
import com.alextos.converter.domain.services.ClipboardService
import converter.composeapp.generated.resources.Res
import converter.composeapp.generated.resources.error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: CurrencyRepository,
    private val storage: StorageService,
    private val delegate: ConverterAppDelegate,
    private val converterUseCase: ConverterUseCase,
    private val clipboardService: ClipboardService,
    private val saveFavouriteCurrenciesUseCase: SaveFavouriteCurrenciesUseCase
): ViewModel() {
    private val _state = MutableStateFlow(MainState(isFABVisible = delegate.isCameraFeatureAvailable))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val isOnboardingFinished = storage.isOnboardingFinished()
            val savedState = storage.getState()
            _state.update { state ->
                state.copy(
                    topText = savedState.topText,
                    bottomText = savedState.bottomText,
                    onboardingState = if (isOnboardingFinished) {
                        OnboardingState(step = OnboardingStep.Done)
                    } else {
                        OnboardingState(
                            step = OnboardingStep.Initial,
                            refreshButtonAlpha = 0f,
                            settingsButtonAlpha = 0f,
                            isNextOnboardingButtonVisible = true,
                            swapButtonAlpha = 0f,
                            bottomEditorAlpha = 0f,
                            hintAlpha = 0f
                        )
                    }
                )
            }

            repository.getCurrencyRates()
                .filter { it.isNotEmpty() }
                .map { list -> list.sortedByDescending { it.isFavourite } }
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
                            contentState = ContentState.Success
                        )
                    }
                    onAction(MainAction.TopTextChanged(state.value.topText))
                    saveFavouriteCurrenciesUseCase(rates)
                }
        }

        fetchData()

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
                fetchData()
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
                            step = OnboardingStep.SettingsButton,
                            refreshButtonAlpha = 0.3f,
                            refreshButtonBackgroundAlpha = 0f,
                            isRefreshButtonTextVisible = false,
                            settingsButtonAlpha = 1f,
                            settingsButtonBackgroundAlpha = 0.1f,
                            isSettingsButtonTextVisible = true,
                        )
                    }
                    OnboardingStep.SettingsButton -> {
                        if (delegate.isCameraFeatureAvailable) {
                            old.copy(
                                step = OnboardingStep.CameraButton,
                                settingsButtonAlpha = 0.3f,
                                settingsButtonBackgroundAlpha = 0f,
                                isSettingsButtonTextVisible = false,
                                isCameraButtonTextVisible = true,
                            )
                        } else {
                            storage.finishOnboarding()
                            old.copy(
                                step = OnboardingStep.Done,
                                topEditorAlpha = 1f,
                                bottomEditorAlpha = 1f,
                                swapButtonAlpha = 1f,
                                refreshButtonAlpha = 1f,
                                settingsButtonAlpha = 1f,
                                settingsButtonBackgroundAlpha = 0f,
                                isCameraButtonTextVisible = false,
                                hintAlpha = 1f,
                                isNextOnboardingButtonVisible = false,
                                isSettingsButtonTextVisible = false,
                            )
                        }
                    }
                    OnboardingStep.CameraButton -> {
                        storage.finishOnboarding()
                        old.copy(
                            step = OnboardingStep.Done,
                            topEditorAlpha = 1f,
                            bottomEditorAlpha = 1f,
                            swapButtonAlpha = 1f,
                            refreshButtonAlpha = 1f,
                            settingsButtonAlpha = 1f,
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

    private fun fetchData() {
        _state.update { state ->
            state.copy(contentState = ContentState.Loading)
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.downloadCurrencyRates()
                _state.update {
                    it.copy(contentState = ContentState.Success)
                }
            } catch (e: Exception) {
                _state.update { state ->
                    if (state.rates.count() > 1) {
                        state.copy(contentState = ContentState.Success)
                    } else {
                        val message = e.message?.let {
                            UiText.DynamicString(it)
                        } ?: UiText.StringResourceId(Res.string.error)
                        state.copy(
                            contentState = ContentState.Error(message)
                        )
                    }
                }
            }
        }
    }
}