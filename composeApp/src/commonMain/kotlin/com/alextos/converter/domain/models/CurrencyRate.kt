package com.alextos.converter.domain.models

import androidx.compose.runtime.Composable
import com.alextos.converter.presentation.extensions.localization
import com.alextos.common.presentation.PickerElement

data class CurrencyRate(
    val code: CurrencyCode,
    val rate: Double,
): PickerElement {
    override val pickerOption: String
        @Composable
        get() = code.localization.asString()

    override val pickerTitle: String
        @Composable
        get() = code.name
}
