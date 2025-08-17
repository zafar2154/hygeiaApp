package com.example.hygeiaapp

import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.camera.core.Camera
import androidx.camera.view.PreviewView
import com.google.android.material.slider.Slider
import java.util.concurrent.Executor

class CameraZoom(private val camera: Camera, private val mainExecutor: Executor) {

    private val cameraInfo = camera.cameraInfo
    private val cameraControl = camera.cameraControl

    /**
     * Menyiapkan fungsionalitas pinch-to-zoom (cubit untuk zoom) pada PreviewView.
     *
     * @param previewView Komponen UI yang menampilkan preview dari kamera.
     */
    fun setupPinchToZoom(previewView: PreviewView) {
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                // Mendapatkan rasio zoom saat ini
                val currentZoomRatio = cameraInfo.zoomState.value?.zoomRatio ?: 1f

                // Menghitung rasio zoom baru berdasarkan gestur cubit
                val delta = detector.scaleFactor
                val newZoomRatio = currentZoomRatio * delta

                // Memastikan rasio zoom baru berada dalam rentang yang valid
                val clampedZoomRatio = newZoomRatio.coerceIn(
                    cameraInfo.zoomState.value?.minZoomRatio ?: 1f,
                    cameraInfo.zoomState.value?.maxZoomRatio ?: 1f
                )

                // Menerapkan zoom ke kamera
                cameraControl.setZoomRatio(clampedZoomRatio)
                return true
            }
        }

        val scaleGestureDetector = ScaleGestureDetector(previewView.context, listener)

        // Menambahkan touch listener ke PreviewView untuk mendeteksi gestur
        previewView.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            // Mengembalikan true agar event sentuhan ditangani di sini
            return@setOnTouchListener true
        }
    }

    /**
     * Menghubungkan Slider untuk mengontrol level zoom kamera.
     *
     * @param slider Komponen Slider dari Material Design.
     */
    fun setupZoomSlider(slider: Slider) {
        // Mengamati perubahan pada ZoomState
        cameraInfo.zoomState.observeForever { zoomState ->
            // Menyesuaikan rentang nilai slider berdasarkan kemampuan zoom kamera
            slider.valueFrom = zoomState.minZoomRatio
            slider.valueTo = zoomState.maxZoomRatio
            // Mengatur posisi slider sesuai dengan zoom saat ini
            slider.value = zoomState.zoomRatio
        }

        // Menambahkan listener untuk mengubah zoom saat slider digeser
        slider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                cameraControl.setZoomRatio(value)
            }
        }
    }
}