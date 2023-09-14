package com.androidisland.todocompose.ui.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

enum class DismissDirection2 {
    RightToLeft, LeftToRight;
}

sealed class DismissThreshold {
    data class Percentage(val value: Float) : DismissThreshold()
    data class Positional(val value: Dp) : DismissThreshold()
}

@Immutable
enum class DismissValue2 {
    Default, DismissedToLeft, DismissedToRight
}

@Immutable
data class DismissState2(
    val isDismissed: Boolean,
    val value: DismissValue2,
    val progress: Float,
    val isThresholdTouched: Boolean
)

private fun DismissThreshold.toPercentage(size: IntSize, density: Density) =
    when (this) {
        is DismissThreshold.Percentage -> {
            value
        }

        is DismissThreshold.Positional -> {
            with(density) {
                value.toPx() / size.width
            }
        }
    }

private fun Float.signedProgressToDismissValue() =
    when {
        this < 0 -> DismissValue2.DismissedToLeft
        this > 0 -> DismissValue2.DismissedToRight
        else -> DismissValue2.Default
    }

@Composable
fun SwipeToDismiss2(
    directions: Set<DismissDirection2>,
    dismissThreshold: (IntSize, Density) -> DismissThreshold = { size, density ->
        DismissThreshold.Percentage(
            0.2f
        )
    },
    isEnabled: Boolean = true,
    dismissDelay: Long = 0L,
    background: @Composable BoxScope.() -> Unit,
    dismissContent: @Composable BoxScope.() -> Unit,
    onDismissStateChange: (DismissState2) -> Unit
) {

    require(directions.isNotEmpty()) {
        "Directions can not be empty"
    }

    val coroutineScope = rememberCoroutineScope()

    val density = LocalDensity.current

    val offsetX = remember { Animatable(0f) }
    var size = remember { IntSize.Zero }
    var signedProgress = remember { 0f }
    var thresholdPercentage = remember { 0f }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Max)
        .onSizeChanged {
            size = it
            thresholdPercentage = dismissThreshold(it, density)
                .toPercentage(it, density)
        }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow), content = background
        )
        Box(modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .draggable(state = rememberDraggableState { delta ->
                val offset = offsetX.value + delta
                if ((offset < 0 && DismissDirection2.RightToLeft in directions) ||
                    offset > 0 && DismissDirection2.LeftToRight in directions
                ) {
                    coroutineScope.launch {
                        offsetX.snapTo(offset)

                        signedProgress = offset / size.width.toFloat()
                        //Update value and direction state
                        val dismissValue = signedProgress.signedProgressToDismissValue()

                        //Invoke listener
                        onDismissStateChange(
                            DismissState2(
                                isDismissed = false,
                                value = dismissValue,
                                progress = abs(signedProgress),
                                isThresholdTouched = abs(signedProgress) >= thresholdPercentage
                            )
                        )
                    }
                }
            },
                orientation = Orientation.Horizontal,
                enabled = isEnabled,
                reverseDirection = false,
                onDragStopped = {
                    coroutineScope.launch {
                        //Not reached to the threshold, so we reset the drag state
                        if (abs(signedProgress) < thresholdPercentage) {
                            offsetX.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(
                                    durationMillis = 200, delayMillis = 0
                                )
                            )
                        } else {
                            val offset = when {
                                signedProgress < 0 -> -size.width
                                signedProgress > 0 -> size.width
                                else -> 0
                            }.toFloat()

                            offsetX.animateTo(
                                targetValue = offset,
                                animationSpec = tween(
                                    durationMillis = 200, delayMillis = 0
                                )
                            )

                            delay(dismissDelay)
                            val isSwipeDismissed = abs(signedProgress) >= thresholdPercentage
                            if (isSwipeDismissed) {
                                val dismissValue = signedProgress.signedProgressToDismissValue()
                                //Invoke listener
                                onDismissStateChange(
                                    DismissState2(
                                        isDismissed = true,
                                        value = dismissValue,
                                        progress = abs(signedProgress),
                                        isThresholdTouched = abs(signedProgress) >= thresholdPercentage
                                    )
                                )
                            }
                        }
                    }
                }), content = dismissContent
        )
    }
}