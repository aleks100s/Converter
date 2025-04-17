package com.alextos.converter.presentation.extensions

import com.alextos.common.UiText
import com.alextos.converter.domain.models.CurrencyCode
import converter.composeapp.generated.resources.Res
import converter.composeapp.generated.resources.*

val CurrencyCode.localization: UiText
    get() = when (this) {
        CurrencyCode.AED -> UiText.StringResourceId(Res.string.currency_aed)
        CurrencyCode.AMD -> UiText.StringResourceId(Res.string.currency_amd)
        CurrencyCode.AUD -> UiText.StringResourceId(Res.string.currency_aud)
        CurrencyCode.AZN -> UiText.StringResourceId(Res.string.currency_azn)
        CurrencyCode.BGN -> UiText.StringResourceId(Res.string.currency_bgn)
        CurrencyCode.BRL -> UiText.StringResourceId(Res.string.currency_brl)
        CurrencyCode.BYN -> UiText.StringResourceId(Res.string.currency_byn)
        CurrencyCode.CAD -> UiText.StringResourceId(Res.string.currency_cad)
        CurrencyCode.CHF -> UiText.StringResourceId(Res.string.currency_chf)
        CurrencyCode.CNY -> UiText.StringResourceId(Res.string.currency_cny)
        CurrencyCode.CZK -> UiText.StringResourceId(Res.string.currency_czk)
        CurrencyCode.DKK -> UiText.StringResourceId(Res.string.currency_dkk)
        CurrencyCode.EGP -> UiText.StringResourceId(Res.string.currency_egp)
        CurrencyCode.EUR -> UiText.StringResourceId(Res.string.currency_eur)
        CurrencyCode.GBP -> UiText.StringResourceId(Res.string.currency_gbp)
        CurrencyCode.GEL -> UiText.StringResourceId(Res.string.currency_gel)
        CurrencyCode.HKD -> UiText.StringResourceId(Res.string.currency_hkd)
        CurrencyCode.HUF -> UiText.StringResourceId(Res.string.currency_huf)
        CurrencyCode.IDR -> UiText.StringResourceId(Res.string.currency_idr)
        CurrencyCode.INR -> UiText.StringResourceId(Res.string.currency_inr)
        CurrencyCode.JPY -> UiText.StringResourceId(Res.string.currency_jpy)
        CurrencyCode.KGS -> UiText.StringResourceId(Res.string.currency_kgs)
        CurrencyCode.KRW -> UiText.StringResourceId(Res.string.currency_krw)
        CurrencyCode.KZT -> UiText.StringResourceId(Res.string.currency_kzt)
        CurrencyCode.MDL -> UiText.StringResourceId(Res.string.currency_mdl)
        CurrencyCode.NOK -> UiText.StringResourceId(Res.string.currency_nok)
        CurrencyCode.NZD -> UiText.StringResourceId(Res.string.currency_nzd)
        CurrencyCode.PLN -> UiText.StringResourceId(Res.string.currency_pln)
        CurrencyCode.QAR -> UiText.StringResourceId(Res.string.currency_qar)
        CurrencyCode.RON -> UiText.StringResourceId(Res.string.currency_ron)
        CurrencyCode.RSD -> UiText.StringResourceId(Res.string.currency_rsd)
        CurrencyCode.RUB -> UiText.StringResourceId(Res.string.currency_rub)
        CurrencyCode.SEK -> UiText.StringResourceId(Res.string.currency_sek)
        CurrencyCode.SGD -> UiText.StringResourceId(Res.string.currency_sgd)
        CurrencyCode.THB -> UiText.StringResourceId(Res.string.currency_thb)
        CurrencyCode.TJS -> UiText.StringResourceId(Res.string.currency_tjs)
        CurrencyCode.TMT -> UiText.StringResourceId(Res.string.currency_tmt)
        CurrencyCode.TRY -> UiText.StringResourceId(Res.string.currency_try)
        CurrencyCode.UAH -> UiText.StringResourceId(Res.string.currency_uah)
        CurrencyCode.USD -> UiText.StringResourceId(Res.string.currency_usd)
        CurrencyCode.UZS -> UiText.StringResourceId(Res.string.currency_uzs)
        CurrencyCode.VND -> UiText.StringResourceId(Res.string.currency_vnd)
        CurrencyCode.ZAR -> UiText.StringResourceId(Res.string.currency_zar)
    }