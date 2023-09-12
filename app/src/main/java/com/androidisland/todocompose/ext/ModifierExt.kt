package com.androidisland.todocompose.ext

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow


private fun interface EventDispatcher<E> {
    fun dispatchEvent(event: E)
}

@Composable
private fun <T> throttleFirstEvents(
    timeoutMillis: Long,
    content: @Composable (EventDispatcher<() -> Unit>) -> T
): T {
    val eventState = remember {
        MutableSharedFlow<() -> Unit>(
            replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    }

    val result = content(EventDispatcher { event ->
        eventState.tryEmit(event)
    })

    LaunchedEffect(true) {
        eventState.throttleFirst(timeoutMillis)
            .collect { onClick ->
                onClick.invoke()
            }
    }
    return result
}

fun Modifier.clickableThrottleFirst(
    timeoutMillis: Long = 500,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = composed(inspectorInfo = debugInspectorInfo {
    name = "clickable"
    properties["enabled"] = enabled
    properties["onClickLabel"] = onClickLabel
    properties["role"] = role
    properties["onClick"] = onClick
}) {
    throttleFirstEvents(timeoutMillis) { eventHandler ->
        Modifier.clickable(enabled = enabled,
            onClickLabel = onClickLabel,
            onClick = { eventHandler.dispatchEvent(onClick) },
            role = role,
            indication = LocalIndication.current,
            interactionSource = remember { MutableInteractionSource() })
    }
}