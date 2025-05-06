//
//  AdBannerView.swift
//  CashbackManager
//
//  Created by Alexander on 25.01.2025.
//

import SwiftUI

struct AdBannerView: View {
	var body: some View {
		_AdBannerView(bannerId: .bannerId)
			.frame(height: 100)
	}
}

private struct _AdBannerView: UIViewControllerRepresentable {
	private let viewController: AdBannerViewController
	
	init(bannerId: String) {
		viewController = AdBannerViewController(bannerId: bannerId)
	}

	func makeUIViewController(context: Context) -> UIViewController {
		let size = viewController.view.systemLayoutSizeFitting(UIView.layoutFittingCompressedSize)
		viewController.preferredContentSize = size
		return viewController
	}

	func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
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
