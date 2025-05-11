package com.alextos.camera

import android.graphics.Rect
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import com.alextos.converter.domain.camera.ConverterUseCase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextAnalyzer(
    private val overlayView: OverlayView,
    private val previewView: PreviewView,
    private val converterUseCase: ConverterUseCase
) : ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }

        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                val boxes = visionText.textBlocks.mapNotNull { block ->
                    converterUseCase.convert(block.text)?.let { result ->
                        val box = makeProportionalBoundingBox(
                            originalBox = block.boundingBox ?: Rect(),
                            originalText = block.text,
                            modifiedText = result
                        )
                        val mappedRect = mapToPreviewView(imageProxy, box)
                        TextBox(rect = mappedRect, text = result)
                    }
                }
                overlayView.setTextBoxes(boxes)
            }
            .addOnFailureListener { e ->
                Log.e("TextAnalyzer", "Ошибка ML Kit: $e")
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun mapToPreviewView(imageProxy: ImageProxy, boundingBox: Rect): Rect {
        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
        val imageWidth = imageProxy.width
        val imageHeight = imageProxy.height

        val viewWidth = previewView.width.toFloat()
        val viewHeight = previewView.height.toFloat()

        val scaleX: Float
        val scaleY: Float

        if (rotationDegrees == 0 || rotationDegrees == 180) {
            scaleX = viewWidth / imageWidth
            scaleY = viewHeight / imageHeight
        } else {
            scaleX = viewWidth / imageHeight
            scaleY = viewHeight / imageWidth
        }

        val left = boundingBox.left * scaleX
        val top = boundingBox.top * scaleY
        val right = boundingBox.right * scaleX
        val bottom = boundingBox.bottom * scaleY

        return Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    }

    private fun makeProportionalBoundingBox(
        originalBox: Rect,
        originalText: String,
        modifiedText: String
    ): Rect {
        if (originalText.isEmpty()) return originalBox

        val originalWidth = originalBox.width()
        val scaleFactor = modifiedText.length.toFloat() / originalText.length

        val newWidth = (originalWidth * scaleFactor) * 2
        val left = originalBox.left
        val top = originalBox.top
        val right = left + newWidth.toInt()
        val bottom = originalBox.bottom

        return Rect(left, top, right, bottom)
    }
}