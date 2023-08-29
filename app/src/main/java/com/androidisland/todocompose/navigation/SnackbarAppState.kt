package com.androidisland.todocompose.navigation

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface SnackbarState {

    fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration,
        result: (SnackbarResult) -> Unit = {}
    )

    fun showSnackbar(
        @StringRes message: Int,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration,
        result: (SnackbarResult) -> Unit = {}
    )
}

class SnackbarAppState(
    private val appContext: Context,
    val hostState: SnackbarHostState,
    val scope: CoroutineScope
) : SnackbarState {
    override fun showSnackbar(
        message: String,
        actionLabel: String?,
        withDismissAction: Boolean,
        duration: SnackbarDuration,
        result: (SnackbarResult) -> Unit
    ) {
        scope.launch {
            hostState.currentSnackbarData?.dismiss()
            result.invoke(
                hostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                    withDismissAction = withDismissAction,
                    duration = duration
                )
            )
        }
    }

    override fun showSnackbar(
        message: Int,
        actionLabel: String?,
        withDismissAction: Boolean,
        duration: SnackbarDuration,
        result: (SnackbarResult) -> Unit
    ) {
        showSnackbar(
            appContext.getString(message),
            actionLabel,
            withDismissAction,
            duration,
            result
        )
    }
}

@Composable
fun rememberSnackbarAppState(
    context: Context = LocalContext.current.applicationContext,
    snackbarHostState: SnackbarHostState = remember {
        SnackbarHostState()
    },
    snackbarScope: CoroutineScope = rememberCoroutineScope()
) = remember(snackbarHostState, snackbarScope) {
    SnackbarAppState(
        appContext = context,
        hostState = snackbarHostState,
        scope = snackbarScope
    )
}