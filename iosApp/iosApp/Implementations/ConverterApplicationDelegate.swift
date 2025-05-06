//
//  ConverterApplicationDelegate.swift
//  iosApp
//
//  Created by Alexander on 25.04.2025.
//

import ComposeApp
import SwiftUI
import UIKit

final class ConverterApplicationDelegate: ConverterAppDelegate {
	private let window: UIWindow

	init(window: UIWindow) {
		self.window = window
	}
	
	func showCamera(converterUseCase: ConverterUseCase, props: CameraProps) {
		let content = CameraWrapper(converterUseCase: converterUseCase)
		let viewController = UIHostingController(rootView: CameraView(props: props, content: content))
		viewController.modalPresentationStyle = .fullScreen
		window.rootViewController?.present(viewController, animated: true)
	}
}
