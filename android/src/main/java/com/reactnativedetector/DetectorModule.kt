package com.rndetector

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule

class DetectorModule(val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ScreenshotDetectionListener, ScreenrecordDetectionListener {
    private val screenshotDetectionDelegate = ScreenshotDetectionDelegate(reactContext, this)
    private val screenrecordDetectionDelegate = ScreenrecordDetectionDelegate(reactContext, this)

    override fun getName(): String {
        return "Detector"
    }

    @ReactMethod
    fun startScreenshotDetection() {
        screenshotDetectionDelegate.start()
    }

    @ReactMethod
    fun stopScreenshotDetection() {
        screenshotDetectionDelegate.stop()
    }

    @ReactMethod
    fun startScreenrecordDetection() {
        screenrecordDetectionDelegate.start()
    }

    @ReactMethod
    fun stopScreenrecordDetection() {
        screenrecordDetectionDelegate.stop()
    }

    override fun onScreenCaptured(path: String) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("UIApplicationUserDidTakeScreenshotNotification", null)
    }

    override fun onScreenRecorded(path: String) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("UIApplicationUserDidTakeScreenRecordNotification", null)
    }
}
