package com.androidisland.todocompose.ext

import android.os.Build
import android.os.Bundle
import android.os.Parcelable


inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else {
        getParcelable(key)
    }