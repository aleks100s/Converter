//
//  CameraViewController.swift
//  iosApp
//
//  Created by Alexander on 25.04.2025.
//

import AVFoundation
import ComposeApp
import Vision
import UIKit

final class CameraViewController: UIViewController, AVCaptureVideoDataOutputSampleBufferDelegate {
	private let session = AVCaptureSession()
	private var previewLayer: AVCaptureVideoPreviewLayer!
	private var textRecognitionRequest = VNRecognizeTextRequest()
	private let overlayView = UIView()
	private let converterUseCase: ConverterUseCase

	init(converterUseCase: ConverterUseCase) {
		self.converterUseCase = converterUseCase
		super.init(nibName: nil, bundle: nil)
	}

	required init?(coder: NSCoder) {
		fatalError("init(coder:) has not been implemented")
	}

	override func viewDidLoad() {
		super.viewDidLoad()
		setupCamera()
		setupVision()
	}

	private func setupCamera() {
		session.sessionPreset = .high
		
		guard let device = AVCaptureDevice.default(for: .video),
			  let input = try? AVCaptureDeviceInput(device: device),
			  session.canAddInput(input)
		else { return }
		
		session.addInput(input)
		
		let output = AVCaptureVideoDataOutput()
		output.setSampleBufferDelegate(self, queue: DispatchQueue(label: "videoQueue"))
		session.addOutput(output)
		
		previewLayer = AVCaptureVideoPreviewLayer(session: session)
		previewLayer.frame = view.bounds
		previewLayer.videoGravity = .resizeAspectFill
		let preview = UIView(frame: view.frame)
		preview.layer.addSublayer(previewLayer)
		overlayView.frame = view.frame
		view.addSubview(preview)
		view.addSubview(overlayView)
		
		Task.detached {
			await self.session.startRunning()
		}
	}

	private func setupVision() {
		textRecognitionRequest = VNRecognizeTextRequest { [weak self] request, error in
			self?.recognizeTextHandler(request: request, error: error)
		}
		textRecognitionRequest.recognitionLevel = .accurate
		textRecognitionRequest.usesLanguageCorrection = true
	}

	@objc(captureOutput:didOutputSampleBuffer:fromConnection:)
	func captureOutput(_ output: AVCaptureOutput,
					   didOutput sampleBuffer: CMSampleBuffer,
					   from connection: AVCaptureConnection) {
		
		guard let pixelBuffer = CMSampleBufferGetImageBuffer(sampleBuffer) else { return }
		
		let requestHandler = VNImageRequestHandler(cvPixelBuffer: pixelBuffer, options: [:])
		try? requestHandler.perform([textRecognitionRequest])
	}

	private func recognizeTextHandler(request: VNRequest, error: Error?) {
		DispatchQueue.main.async {
			// Удаляем старые оверлеи
			self.overlayView.subviews.forEach { subview in
				subview.removeFromSuperview()
			}
			self.overlayView.layer.sublayers?.removeAll(where: { $0.name == "TextOverlay" })
		}

		guard let results = request.results as? [VNRecognizedTextObservation] else { return }
		
		for observation in results {
			guard let candidate = observation.topCandidates(1).first else { continue }

			guard let result = converterUseCase.convert(value: candidate.string) else { continue }

			DispatchQueue.main.async {
				let rect = self.transformBoundingBox(observation.boundingBox, length: result.count)
				let borderLayer = self.createTextLayer(frame: rect, text: result)
				self.overlayView.layer.addSublayer(borderLayer)
			}
		}
	}

	private func transformBoundingBox(_ boundingBox: CGRect, length: Int) -> CGRect {
		let viewSize = view.frame.size
		// let width = boundingBox.width * viewSize.height
		let height = boundingBox.height * viewSize.width
		let x = (boundingBox.origin.y - boundingBox.height) * viewSize.width
		let y = boundingBox.origin.x * viewSize.height

		return CGRect(x: x, y: y, width: height * CGFloat(length) / 2, height: height)
	}

	private func createTextLayer(frame: CGRect, text: String) -> CALayer {
		let layer = CATextLayer()
		layer.string = text
		layer.name = "TextOverlay"
		layer.frame = frame
		layer.fontSize = frame.height - 4
		layer.backgroundColor = UIColor.white.cgColor
		layer.foregroundColor = UIColor.black.cgColor
		return layer
	}
}
