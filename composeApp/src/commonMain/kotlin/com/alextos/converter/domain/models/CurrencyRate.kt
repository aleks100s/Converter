package com.alextos.converter.domain.models

import com.alextos.common.UiText
import com.alextos.common.presentation.PickerElement
import com.alextos.converter.presentation.extensions.localization

data class CurrencyRate(
    val code: CurrencyCode,
    val isFavourite: Boolean,
    val isMain: Boolean,
    val rate: Double,
    val flag: String,
    val sign: String,
): PickerElement {
    override val pickerOption: UiText
        get() = code.localization

    override val pickerTitle: String
        get() = code.name

    override val trailingIcon: PickerElement.Icon?
        get() = if (isMain) PickerElement.Icon.Star else null
}
