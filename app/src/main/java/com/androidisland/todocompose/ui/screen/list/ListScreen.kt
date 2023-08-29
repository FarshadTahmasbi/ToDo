package com.androidisland.todocompose.ui.screen.list

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.androidisland.todocompose.R
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.ui.common.CustomSnackbarHost
import com.androidisland.todocompose.ui.common.SnackbarState
import com.androidisland.todocompose.ui.common.rememberSnackbarState
import com.androidisland.todocompose.ui.theme.ToDoAppTheme
import com.androidisland.todocompose.ui.viewmodel.SharedViewModel
import com.androidisland.todocompose.util.Action
import com.androidisland.todocompose.util.Either


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    action: Action?,
    toDoTask: ToDoTask?,
    viewModel: SharedViewModel,
    navigateToTaskScreen: (Int) -> Unit
) {
    val allTasksResource by viewModel.allTasks.collectAsState()
    val snackbarAppState = rememberSnackbarState()

    LaunchedEffect(key1 = Unit) {
        if (action != null && toDoTask != null) {
            handleAction(action, toDoTask, snackbarAppState, viewModel)
        }
    }

    Scaffold(topBar = {
        ListAppBar(onSortClicked = {
            //TODO sort
            Log.d("test123", "Sort: $it")
        }, onDeleteClicked = {
            //TODO delete
            Log.d("test123", "Delete")
        }, onSearchClicked = {
            //TODO search for it
            Log.d("test123", "Searching for $it")
        })
    },
        snackbarHost = { CustomSnackbarHost(hostState = snackbarAppState.hostState) },
        content = {
            ListContent(
                tasksResource = allTasksResource,
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
            action = null,
            toDoTask = null,
            viewModel = hiltViewModel(),
            navigateToTaskScreen = {},
        )
    }
}

private fun handleAction(
    action: Action,
    toDoTask: ToDoTask,
    snackbarState: SnackbarState,
    viewModel: SharedViewModel
) {
    snackbarState.dismiss()
    when (action) {
        Action.ADD -> {
            viewModel.addTask(toDoTask)
            snackbarState.showSnackbar(Either.Right(R.string.task_add_msg))
        }

        Action.UPDATE -> {
            viewModel.updateTask(toDoTask)
            snackbarState.showSnackbar(Either.Right(R.string.task_update_msg))
        }

        Action.DELETE -> {
            viewModel.deleteTask(toDoTask)
            snackbarState.showSnackbar(
                Either.Right(R.string.task_delete_msg), actionLabel = "Undo"
            ) { result ->
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.addTask(toDoTask)
                }
            }
        }

        Action.DELETE_ALL -> {

        }

        else -> Unit
    }
}
