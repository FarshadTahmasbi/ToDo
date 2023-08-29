package com.androidisland.todocompose.navigation

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.androidisland.todocompose.util.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class SnackbarAppState(
    private val appContext: Context,
    var hostState: SnackbarHostState,
    var scope: CoroutineScope
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