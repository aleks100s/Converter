package com.alextos.common.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.alextos.BuildConfig
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import kotlin.math.roundToInt

@Composable
actual fun NativeBanner() {
    val localContext = LocalContext.current
    val displayMetrics = remember { localContext.resources.displayMetrics }

    AndroidView(
        factory = { context ->
            val banner = BannerAdView(context)
            banner.setAdUnitId(if (BuildConfig.DEBUG) "demo-banner-yandex" else "R-M-15379111-1")
            val width = displayMetrics.widthPixels
            val adWidth = (width / displayMetrics.density).roundToInt()
            banner.setAdSize(BannerAdSize.stickySize(context = context, width = adWidth))
            banner.setBannerAdEventListener(object : BannerAdEventListener {
                override fun onAdClicked() {
                    println("onAdClicked")
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    println("onAdFailedToLoad")
                }

                override fun onAdLoaded() {
                    println("onAdLoaded")
                }

                override fun onImpression(impressionData: ImpressionData?) {
                    println("onImpression")
                }

                override fun onLeftApplication() {
                    println("onLeftApplication")
                }

                override fun onReturnedToApplication() {
                    println("onReturnedToApplication")
                }
            })
            banner.loadAd(AdRequest.Builder().build())
            banner
        },
        update = { view ->
            val width = displayMetrics.widthPixels
            val adWidth = (width / displayMetrics.density).roundToInt()
            view.setAdSize(BannerAdSize.stickySize(context = localContext, width = adWidth))
        }
    )
}