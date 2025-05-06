package com.alextos.common

import platform.UIKit.UIViewController

interface NativeViewFactory {
    fun createBannerView(): UIViewController
}