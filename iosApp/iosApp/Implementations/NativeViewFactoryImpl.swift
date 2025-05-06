//
//  NativeViewFactoryImpl.swift
//  iosApp
//
//  Created by Alexander on 06.05.2025.
//

import SwiftUI
import ComposeApp

final class NativeViewFactoryImpl: NativeViewFactory {
	func createBannerView() -> UIViewController {
		UIHostingController(rootView: AdBannerView(bannerId: .bannerId))
	}
}

private extension String {
	static var bannerId: String {
	#if DEBUG
		"demo-banner-yandex"
	#else
		"R-M-15379111-1"
	#endif
	}
}
