package com.androidisland.todocompose.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * This is Unscaled independent pixel, just like dp.
 * Use it for text size to prevent size change when user change font size in settings
 */
val Number.usp: TextUnit
    @Composable
    get() {
        val value = toFloat()
        return with(LocalDensity.current) {
            (value / fontScale).sp
        }
    }
