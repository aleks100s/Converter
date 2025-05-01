//
//  ConverterApplicationDelegate.swift
//  iosApp
//
//  Created by Alexander on 25.04.2025.
//

import ComposeApp
import UIKit

final class ConverterApplicationDelegate: ConverterAppDelegate {
	private let window: UIWindow

	init(window: UIWindow) {
		self.window = window
	}
	
	func showCamera(converterUseCase: ConverterUseCase) {
		let controller = CameraViewController(converterUseCase: converterUseCase)
		controller.navigationItem.rightBarButtonItem = UIBarButtonItem(systemItem: .cancel, primaryAction: UIAction(handler: { [weak controller] _ in
			controller?.dismiss(animated: true)
		}))
		let navigationController = UINavigationController(rootViewController: controller)
		window.rootViewController?.present(navigationController, animated: true)
	}
}
