package com.alextos.common

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed interface UiText {
    data class DynamicString(val value: String): UiText
    class StringResourceId(
        val id: StringResource,
        vararg val args: Any
    ): UiText

    @Composable
    fun asString(): String {
        return when(this) {
            is DynamicString -> value
            is StringResourceId -> stringResource(id, args)
        }
    }
}