package com.androidisland.todocompose.ui.screen.list

import android.content.Context
import android.util.Log
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.androidisland.todocompose.ui.common.CustomSnackbarHost
import com.androidisland.todocompose.ui.common.SnackbarState
import com.androidisland.todocompose.ui.common.rememberSnackbarState
import com.androidisland.todocompose.ui.theme.ToDoAppTheme
import com.androidisland.todocompose.ui.viewmodel.SharedViewModel
import com.androidisland.todocompose.util.Action
import com.androidisland.todocompose.util.ActionEvent
import com.androidisland.todocompose.util.Either


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    sharedViewModel: SharedViewModel,
    navigateToTaskScreen: (Int) -> Unit
) {
    val taskQueryResult by sharedViewModel.queriedTasks.collectAsState()
    val snackbarAppState = rememberSnackbarState()
    val searchQuery by sharedViewModel.searchQuery.collectAsState()

    val context = LocalContext.current
    val actionEvent by sharedViewModel.actionEvent.collectAsState(initial = null)
    ActionEventsLaunchedEffect(context, actionEvent, snackbarAppState, sharedViewModel)

    Scaffold(
        topBar = {
            ListAppBar(
                searchQuery = searchQuery,
                onSortClicked = {
                    //TODO sort
                    Log.d("test123", "Sort: $it")
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
                tasksResource = taskQueryResult,
                navigateToTaskScreen = navigateToTaskScreen,
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
