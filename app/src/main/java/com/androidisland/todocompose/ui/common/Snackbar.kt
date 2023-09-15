package com.androidisland.todocompose.ui.common

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.androidisland.todocompose.util.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class SnackbarState(
    private val appContext: Context,
    val hostState: SnackbarHostState,
    private val scope: CoroutineScope
) {

    fun showSnackbar(
        message: Either<String, Int>,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short,
        result: (SnackbarResult) -> Unit = {}
    ) {
        message.fold({
            showSnackbar(it, actionLabel, withDismissAction, duration, result)
        }, {
            showSnackbar(it, actionLabel, withDismissAction, duration, result)
        })
    }

    fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short,
        result: (SnackbarResult) -> Unit = {}
    ) {
        hostState.currentSnackbarData?.dismiss()
        scope.launch {
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

    fun showSnackbar(
        message: Int,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short,
        result: (SnackbarResult) -> Unit = {}
    ) {
        showSnackbar(
            appContext.getString(message),
            actionLabel,
            withDismissAction,
            duration,
            result
        )
    }

    fun dismiss() {
        hostState.currentSnackbarData?.dismiss()
    }
}

@Composable
fun rememberSnackbarState(
    context: Context = LocalContext.current.applicationContext,
    snackbarHostState: SnackbarHostState = remember {
        SnackbarHostState()
    },
    snackbarScope: CoroutineScope = rememberCoroutineScope()
) = remember(snackbarHostState, snackbarScope) {
    SnackbarState(
        appContext = context,
        hostState = snackbarHostState,
        scope = snackbarScope
    )
}

@Composable
fun CustomSnackbarHost(
    modifier: Modifier = Modifier,
    hostState: SnackbarHostState,
    alignment: Alignment = Alignment.BottomCenter,
    snackbar: @Composable (SnackbarData) -> Unit = {
        Snackbar(
            snackbarData = it,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    }
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = alignment
    ) {
        SnackbarHost(
            modifier = modifier,
            hostState = hostState,
            snackbar = snackbar
        )
    }
}