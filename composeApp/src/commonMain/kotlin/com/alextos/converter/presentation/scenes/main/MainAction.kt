package com.alextos.converter.presentation.scenes.main

import com.alextos.converter.domain.camera.CameraProps
import com.alextos.converter.domain.models.CurrencyRate

sealed interface MainAction {
    data class TopTextChanged(val text: String) : MainAction
    data class BottomTextChanged(val text: String) : MainAction
    data object SwapCurrencies : MainAction
    data class TopCurrencySelected(val currency: CurrencyRate) : MainAction
    data class BottomCurrencySelected(val currency: CurrencyRate) : MainAction
    data object ReloadRates : MainAction
    data class ShowCamera(val props: CameraProps): MainAction
    data class CopyButtonTapped(val text: String, val label: String): MainAction
    data object NextOnboardingStepButtonTapped : MainAction
    data object SettingsButtonTapped : MainAction
    data object DismissSettingsSheet : MainAction
}