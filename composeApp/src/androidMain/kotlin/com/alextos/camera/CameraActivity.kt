package com.alextos.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.alextos.converter.domain.camera.ConverterUseCase
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin

class CameraActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var overlayView: OverlayView
    private lateinit var bottomButton: Button
    private lateinit var converterUseCase: ConverterUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        converterUseCase = getKoin().get()
        title = intent.getStringExtra("title") ?: ""
        setContentView(com.alextos.R.layout.activity_camera)

        previewView = findViewById(com.alextos.R.id.previewView)
        overlayView = findViewById(com.alextos.R.id.overlayView)
        bottomButton = findViewById(com.alextos.R.id.bottomButton)
        bottomButton.text = intent.getStringExtra("button") ?: ""
        bottomButton.setOnClickListener {
            finish()
        }

        if (allPermissionsGranted()) {
            lifecycleScope.launch {
                startCamera()
            }
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Все разрешения получены — запускаем камеру
                lifecycleScope.launch {
                    startCamera()
                }
            } else {
                // Не все разрешения даны — покажем сообщение и закроем
                finish()
            }
        }
    }

    private suspend fun startCamera() {
        val cameraProvider = ProcessCameraProvider.awaitInstance(this)

        val preview = Preview.Builder().build().also {
            it.surfaceProvider = previewView.surfaceProvider
        }

        val imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(ContextCompat.getMainExecutor(this), TextAnalyzer(overlayView, previewView, converterUseCase))
            }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            this, cameraSelector, preview, imageAnalyzer
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}