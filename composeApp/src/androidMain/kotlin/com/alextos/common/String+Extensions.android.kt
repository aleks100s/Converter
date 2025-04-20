package com.alextos.common

actual fun Double.toFullString(): String {
    return String.format("%f", this)
}