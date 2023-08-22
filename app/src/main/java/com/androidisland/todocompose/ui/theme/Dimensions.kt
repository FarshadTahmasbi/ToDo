package com.androidisland.todocompose.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp

object Dimensions {
    val largePadding = 12.dp
    val mediumPadding = 8.dp
    val smallPadding = 6.dp

    val priorityIndicatorSize = 16.dp
}

val MaterialTheme.dimens: Dimensions
    get() = Dimensions