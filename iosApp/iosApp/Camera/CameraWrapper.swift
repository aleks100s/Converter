//
//  CameraWrapper.swift
//  iosApp
//
//  Created by Alexander on 03.05.2025.
//

import ComposeApp
import SwiftUI

struct CameraWrapper: UIViewControllerRepresentable {
	let converterUseCase: ConverterUseCase

	func makeUIViewController(context: Context) -> CameraViewController {
		return CameraViewController(converterUseCase: converterUseCase)
	}

	func updateUIViewController(_ uiViewController: CameraViewController, context: Context) {
		// Обновление при изменении SwiftUI-состояния
	}
}
