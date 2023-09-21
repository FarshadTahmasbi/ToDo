package com.androidisland.todocompose.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp

object Dimensions {
    val xxLargePadding = 24.dp
    val xLargePadding = 16.dp
    val largePadding = 12.dp
    val mediumPadding = 8.dp
    val smallPadding = 6.dp

    val priorityIndicatorSize = 16.dp
    val topAppBarHeight = 64.dp

    val elevationSmall = 2.0.dp
    val elevationMedium = 4.0.dp
    val elevationLarge = 8.0.dp

    val iconSizeSmall = 16.dp
    val iconSizeMedium = 32.dp
    val iconSizeLarge = 64.dp
    val iconSizeXLarge = 96.dp

    val borderWidthSmall = 1.dp
    val borderWidthMedium = 2.dp
}

const val alphaDisabled = 0.5f

val MaterialTheme.dimens: Dimensions
    get() = Dimensions