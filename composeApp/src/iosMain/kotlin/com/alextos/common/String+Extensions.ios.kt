package com.alextos.common

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual fun Double.toFullString(): String {
    return NSString.stringWithFormat("%f", this)
}