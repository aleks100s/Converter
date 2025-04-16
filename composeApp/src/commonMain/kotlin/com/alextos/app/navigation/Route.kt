package com.alextos.app.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Converter: Route
}