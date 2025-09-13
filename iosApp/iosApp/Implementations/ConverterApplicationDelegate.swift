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
    private let userDefaults: UserDefaults
	private let window: UIWindow

	init(window: UIWindow, userDefaults: UserDefaults) {
		self.window = window
        self.userDefaults = userDefaults
	}
	
	func showCamera(converterUseCase: ConverterUseCase, props: CameraProps) {
		let content = CameraWrapper(converterUseCase: converterUseCase)
		let viewController = UIHostingController(rootView: CameraView(props: props, content: content))
		viewController.modalPresentationStyle = .fullScreen
		window.rootViewController?.present(viewController, animated: true)
	}
    
    func updateFavouriteRates(result: GetFavouriteCurrencyRatesUseCase.Result?) {
        if let result {
            let favourites = FavouriteRatesData(result: result)
            userDefaults.set(try? JSONEncoder().encode(favourites), forKey: "favourites")
        } else {
            userDefaults.removeObject(forKey: "favourites")
        }
    }
}
