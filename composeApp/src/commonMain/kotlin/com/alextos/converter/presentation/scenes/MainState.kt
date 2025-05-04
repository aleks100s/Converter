package com.alextos.converter.presentation.scenes

import com.alextos.converter.domain.models.CurrencyRate

data class MainState(
    val rates: List<CurrencyRate> = emptyList(),
    val topText: String = "1",
    val bottomText: String = "100",
    val topCurrency: CurrencyRate? = null,
    val bottomCurrency: CurrencyRate? = null,
    val isLoading: Boolean = true,
    val onboardingState: OnboardingState = OnboardingState(),
)

data class OnboardingState(
    val step: OnboardingStep = OnboardingStep.Done,
    val topEditorAlpha: Float = 1f,
    val topEditorBackgroundAlpha: Float = 0.0f,
    val isTopEditorTextVisible: Boolean = false,
    val bottomEditorAlpha: Float = 1f,
    val bottomEditorBackgroundAlpha: Float = 0.0f,
    val isBottomEditorTextVisible: Boolean = false,
    val swapButtonAlpha: Float = 1f,
    val swapButtonBackgroundAlpha: Float = 0.0f,
    val isSwapButtonTextVisible: Boolean = false,
    val refreshButtonAlpha: Float = 1f,
    val refreshButtonBackgroundAlpha: Float = 0.0f,
    val isRefreshButtonTextVisible: Boolean = false,
    val isCameraButtonTextVisible: Boolean = false,
    val hintAlpha: Float = 1f,
    val isNextOnboardingButtonVisible: Boolean = false,
)

enum class OnboardingStep {
    Initial,
    TopEditor,
    BottomEditor,
    SwapButton,
    RefreshButton,
    CameraButton,
    Done
}