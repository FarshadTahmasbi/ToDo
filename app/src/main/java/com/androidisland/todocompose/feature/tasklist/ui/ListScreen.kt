package com.androidisland.todocompose.feature.tasklist.ui

import android.content.Context
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.androidisland.todocompose.R
import com.androidisland.todocompose.common.SharedViewModel
import com.androidisland.todocompose.common.theme.ToDoAppTheme
import com.androidisland.todocompose.common.ui.CustomSnackbarHost
import com.androidisland.todocompose.common.ui.SnackbarState
import com.androidisland.todocompose.common.ui.rememberSnackbarState
import com.androidisland.todocompose.util.Action
import com.androidisland.todocompose.util.ActionEvent
import com.androidisland.todocompose.util.Either


@Composable
fun ListScreen(
    sharedViewModel: SharedViewModel,
    navigateToTaskScreen: (Int) -> Unit
) {
    val snackbarAppState = rememberSnackbarState()

    val searchQuery by sharedViewModel.searchQuery.collectAsState()
    val sortState by sharedViewModel.sortState.collectAsState()

    val context = LocalContext.current
    val actionEvent by sharedViewModel.actionEvent.collectAsState(initial = null)
    ActionEventsLaunchedEffect(context, actionEvent, snackbarAppState, sharedViewModel)

    Scaffold(
        topBar = {
            ListAppBar(
                searchQuery = searchQuery,
                selectedPriority = sortState,
                onSortClicked = { priority ->
                    sharedViewModel.persistSortState(priority)
                }, onDeleteAllClicked = {
                    sharedViewModel.sendActionEvent(Action.DELETE_ALL, null)
                }, onSearchClicked = {
                    sharedViewModel.queryTasks(it)
                }, onCloseClicked = {
                    sharedViewModel.queryTasks(null)
                })
        },
        snackbarHost = { CustomSnackbarHost(hostState = snackbarAppState.hostState) },
        content = {
            ListContent(
                sortState = sortState,
                isInSearchMode = searchQuery != null,
                sharedViewModel = sharedViewModel,
                navigateToTaskScreen = navigateToTaskScreen,
                onSwipeDismiss = { action, toDoTask ->
                    sharedViewModel.sendActionEvent(action, toDoTask)
                },
                contentPadding = it
            )
        },
        floatingActionButton = { ListFab(navigateToTaskScreen) })
}

@Composable
fun ListFab(
    onFabClicked: (Int) -> Unit
) {
    FloatingActionButton(
        onClick = { onFabClicked(-1) }, shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Filled.Add, contentDescription = stringResource(
                id = R.string.add_button
            ), tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
private fun ListScreenPreview() {
    ToDoAppTheme {
        ListScreen(
            sharedViewModel = hiltViewModel(),
            navigateToTaskScreen = {},
        )
    }
}

@Composable
private fun ActionEventsLaunchedEffect(
    context: Context,
    event: ActionEvent?,
    snackbarState: SnackbarState,
    viewModel: SharedViewModel
) {
    LaunchedEffect(key1 = event) {
        if (event == null) return@LaunchedEffect
        snackbarState.dismiss()
        val (action, toDoTask) = event
        when (action) {
            Action.ADD -> {
                viewModel.addTask(toDoTask!!)
                snackbarState.showSnackbar(Either.Right(R.string.task_add_msg))
            }

            Action.UPDATE -> {
                viewModel.updateTask(toDoTask!!)
                snackbarState.showSnackbar(Either.Right(R.string.task_update_msg))
            }

            Action.DELETE -> {
                viewModel.deleteTask(toDoTask!!)
                snackbarState.showSnackbar(
                    Either.Right(R.string.task_delete_msg),
                    actionLabel = context.getString(R.string.action_undo)
                ) { result ->
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.addTask(toDoTask)
                    }
                }
            }

            Action.DELETE_ALL -> {
                viewModel.deleteAllTasks()
                snackbarState.showSnackbar(
                    R.string.delete_all_msg,
                    actionLabel = context.getString(R.string.action_ok)
                ) { result ->
                    if (result == SnackbarResult.ActionPerformed) {
                        snackbarState.dismiss()
                    }
                }
            }

            else -> Unit
        }
    }
}
