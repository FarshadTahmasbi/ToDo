package com.androidisland.todocompose.ext

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat

inline fun <reified T> Context.getSystemService() =
    ContextCompat.getSystemService(this, T::class.java)

@RequiresPermission(Manifest.permission.VIBRATE)
fun Vibrator.vibrateOneShot(millis: Long) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrate(VibrationEffect.createOneShot(millis, 255))
    } else {
        vibrate(millis)
    }
}