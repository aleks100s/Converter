package com.alextos.converter.domain.models

val CurrencyCode.symbol: String
    get() {
        return when (this) {
            CurrencyCode.AED -> "D" // United Arab Emirates Dirham,
            CurrencyCode.AMD -> "Դ" // Armenian Dram,
            CurrencyCode.AUD -> "A\$" // Australian Dollar,
            CurrencyCode.AZN -> "₼" // Azerbaijan Manat,
            CurrencyCode.BGN -> "лв" // Bulgarian Lev,
            CurrencyCode.BRL -> "R\$" // Brazilian Real,
            CurrencyCode.BYN -> "Br" // Belarusian Ruble,
            CurrencyCode.CAD -> "C\$" // Canadian Dollar,
            CurrencyCode.CHF -> "₣" // Swiss Franc,
            CurrencyCode.CNY -> "¥" // Chinese Yuan,
            CurrencyCode.CZK -> "Kč" // Czech Koruna,
            CurrencyCode.DKK -> "kr." // Danish Krone,
            CurrencyCode.EGP -> "E£" // Egyptian Pound,
            CurrencyCode.EUR -> "€" // Euro,
            CurrencyCode.GBP -> "£" // British Pound Sterling,
            CurrencyCode.GEL -> "ლ" // Georgian Lari,
            CurrencyCode.HKD -> "HK\$" // Hong Kong Dollar,
            CurrencyCode.HUF -> "Ft" // Hungarian Forint,
            CurrencyCode.IDR -> "Rp" // Indonesian Rupiah,
            CurrencyCode.INR -> "₹" // Indian Rupee,
            CurrencyCode.JPY -> "¥" // Japanese Yen,
            CurrencyCode.KGS -> "лв" // Kyrgyzstani Som,
            CurrencyCode.KRW -> "₩" // South Korean Won,
            CurrencyCode.KZT -> "₸" // Kazakhstani Tenge,
            CurrencyCode.MDL -> "L" // Moldovan Leu,
            CurrencyCode.NOK -> "kr" // Norwegian Krone,
            CurrencyCode.NZD -> "NZ\$" // New Zealand Dollar,
            CurrencyCode.PLN -> "zł" // Polish Zloty,
            CurrencyCode.QAR -> "QR" // Qatari Rial,
            CurrencyCode.RON -> "lei" // Romanian Leu,
            CurrencyCode.RSD -> "РСД" // Serbian Dinar,
            CurrencyCode.RUB -> "₽" // Russian Ruble,
            CurrencyCode.SEK -> "kr" // Swedish Krona,
            CurrencyCode.SGD -> "S\$" // Singapore Dollar,
            CurrencyCode.THB -> "฿" // Thai Baht,
            CurrencyCode.TJS -> "SM" // Tajikistani Somoni,
            CurrencyCode.TMT -> "m" // Turkmenistani Manat,
            CurrencyCode.TRY -> "₺" // Turkish Lira,
            CurrencyCode.UAH -> "₴" // Ukrainian Hryvnia,
            CurrencyCode.USD -> "$" // United States Dollar,
            CurrencyCode.UZS -> "лв" // Uzbekistani Som,
            CurrencyCode.VND -> "₫" // Vietnamese Dong,
            CurrencyCode.ZAR -> "R" // South African Rand,
        }
    }