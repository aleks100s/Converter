package com.alextos.common

fun Double.preciseFormat(): String {
    val str = this.toString()

    // если число целое, то отбрасываем дробную часть
    if (str.endsWith(".0")) return str.substringBefore(".")

    val parts = str.split(".")
    val integer = parts[0]
    val fraction = parts.getOrNull(1) ?: return integer

    // если дробная часть вся из нулей — вернуть только целую часть
    if (fraction.all { it == '0' }) return integer

    // найти первую и вторую значимые цифры
    var significantDigits = 0
    var endIndex = 0
    while (endIndex < fraction.length && significantDigits < 2) {
        if (fraction[endIndex] != '0') significantDigits++
        endIndex++
    }

    // если значимых цифр меньше 2 — вернуть всю дробную часть
    val trimmed = fraction.take(endIndex).trimEnd('0')

    return if (trimmed.isEmpty()) integer else "$integer.$trimmed"
}