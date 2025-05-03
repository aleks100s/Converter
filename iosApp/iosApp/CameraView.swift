//
//  CameraView.swift
//  iosApp
//
//  Created by Alexander on 03.05.2025.
//

import ComposeApp
import SwiftUI

struct CameraView<Content: View>: View {
	let props: CameraProps
	let content: Content
	
	@Environment(\.dismiss)
	private var dismiss

	var body: some View {
		content
			.ignoresSafeArea(.all)
			.overlay(alignment: .top) {
				Text(props.title)
					.frame(maxWidth: .infinity, alignment: .center)
					.padding(.bottom, 8)
					.background(.regularMaterial)
			}
			.toolbar {
				ToolbarItem(placement: .bottomBar) {
					Button(props.button) {
						dismiss()
					}
				}
				
				ToolbarItem(placement: .topBarTrailing) {
					Button(props.button, systemImage: "cross") {
						dismiss()
					}
				}
			}
	}
}
