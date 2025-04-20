package com.alextos.converter.domain.models

import androidx.compose.runtime.Composable
import com.alextos.common.presentation.PickerElement
import com.alextos.converter.presentation.extensions.localization

data class CurrencyRate(
    val code: CurrencyCode,
    val priority: Int,
    val isFavourite: Boolean,
    val isMain: Boolean,
    val rate: Double,
    val flag: String,
    val sign: String,
): PickerElement {
    override val pickerOption: String
        @Composable
        get() = code.localization.asString()

    override val pickerTitle: String
        @Composable
        get() = code.name
}
