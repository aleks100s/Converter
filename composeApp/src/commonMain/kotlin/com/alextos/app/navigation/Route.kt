package com.alextos.app.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Converter: Route

    @Serializable
    data object Settings: Route

    @Serializable
    data object MainCurrencySettings: Route

    @Serializable
    data object FavouriteCurrencySettings: Route
}