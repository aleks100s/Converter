//
//  ConverterApplicationDelegate.swift
//  iosApp
//
//  Created by Alexander on 25.04.2025.
//

import ComposeApp
import SwiftUI
import WidgetKit

final class ConverterApplicationDelegate: ConverterAppDelegate {
    var isCameraFeatureAvailable = true
    
    private let userDefaults: UserDefaults
	private let window: UIWindow
    private let spotlightService: SpotlightService

	init(
        window: UIWindow,
        userDefaults: UserDefaults,
        spotlightService: SpotlightService
    ) {
		self.window = window
        self.userDefaults = userDefaults
        self.spotlightService = spotlightService
	}
	
	func showCamera(converterUseCase: ConverterUseCase, props: CameraProps) {
		let content = CameraWrapper(converterUseCase: converterUseCase)
		let viewController = UIHostingController(rootView: CameraView(props: props, content: content))
		viewController.modalPresentationStyle = .fullScreen
		window.rootViewController?.present(viewController, animated: true)
	}
    
    func updateCurrencies(favourites: [String], main: String?) {
        let data = SharedWidgetData(favouriteCurrencies: favourites, mainCurrency: main)
        userDefaults.set(try? JSONEncoder().encode(data), forKey: Constants.widgetDataKey)
        WidgetCenter.shared.reloadAllTimelines()
    }
    
    func lastQuery(state: ConverterState) {
        spotlightService.trackLastQuery(
            currencyFrom: state.topCurrency?.name ?? "",
            currencyTo: state.bottomCurrency?.name ?? "",
            amountFrom: state.topText,
            amountTo: state.bottomText
        )
    }
}
