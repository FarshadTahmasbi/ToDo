package com.androidisland.todocompose.feature.tasklist.ui

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.androidisland.todocompose.R
import com.androidisland.todocompose.arch.CollectEffects
import com.androidisland.todocompose.arch.CommonEffect
import com.androidisland.todocompose.common.SharedViewModel
import com.androidisland.todocompose.common.theme.ToDoAppTheme
import com.androidisland.todocompose.common.ui.CustomSnackbarHost
import com.androidisland.todocompose.common.ui.SnackbarState
import com.androidisland.todocompose.common.ui.rememberSnackbarState
import com.androidisland.todocompose.feature.tasklist.TaskListContract
import kotlinx.coroutines.flow.Flow


@Composable
fun TaskListScreen(
    viewModel: SharedViewModel,
    navigateToTaskScreen: (Int) -> Unit
) {
    val taskListState by viewModel.state.collectAsState()
    val snackbarAppState = rememberSnackbarState()

    HandleEffects(viewModel.effect, snackbarAppState, navigateToTaskScreen)

    Scaffold(
        topBar = {
            TaskListAppBar(
                searchQuery = taskListState.query,
                sort = taskListState.sort,
                onSortClicked = { priority ->
                    viewModel.persistSortState(priority)
                }, onDeleteAllClicked = {
                    viewModel.deleteAllTasks()
                }, onSearchClicked = {
                    viewModel.queryTasks(it)
                }, onCloseClicked = {
                    viewModel.queryTasks(null)
                })
        },
        snackbarHost = { CustomSnackbarHost(hostState = snackbarAppState.hostState) },
        content = {
            TaskListContent(
                state = taskListState,
                navigateToTaskScreen = navigateToTaskScreen,
                onSwipeDismiss = viewModel::deleteTask,
                contentPadding = it
            )
        },
        floatingActionButton = { TaskListFab(navigateToTaskScreen) })
}

@Composable
fun HandleEffects(
    effects: Flow<CommonEffect>,
    snackbarAppState: SnackbarState,
    navigateToTaskScreen: (Int) -> Unit
) {
    CollectEffects(effect = effects) { effect ->
        when (effect) {
            is CommonEffect.ShowSnackBar -> snackbarAppState.showSnackbar(
                message = effect.message,
                duration = effect.duration
            )

            is CommonEffect.ShowSnackBarWithAction -> snackbarAppState.showSnackbar(
                message = effect.message,
                duration = effect.duration,
                actionLabel = effect.action,
                result = effect.onResult
            )

            is TaskListContract.Effect.NavigateToTask -> navigateToTaskScreen(effect.id)
            else -> Unit
        }
    }
}

@Composable
fun TaskListFab(
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
private fun TaskListScreenPreview() {
    ToDoAppTheme {
        TaskListScreen(
            viewModel = hiltViewModel(),
            navigateToTaskScreen = {},
        )
    }
}

//@Composable
//private fun ActionEventsLaunchedEffect(
//    context: Context,
//    event: ActionEvent?,
//    snackbarState: SnackbarState,
//    viewModel: TaskListViewModel
//) {
//    LaunchedEffect(key1 = event) {
//        if (event == null) return@LaunchedEffect
//        snackbarState.dismiss()
//        val (action, toDoTask) = event
//        when (action) {
//            Action.ADD -> {
//                viewModel.insertOrUpdateTask(toDoTask!!)
//                snackbarState.showSnackbar(Either.Right(R.string.task_insert_msg))
//            }
//
//            Action.UPDATE -> {
//                viewModel.updateTask(toDoTask!!)
//                snackbarState.showSnackbar(Either.Right(R.string.task_update_msg))
//            }
//
//            Action.DELETE -> {
//                viewModel.deleteTask(toDoTask!!)
//                snackbarState.showSnackbar(
//                    Either.Right(R.string.task_delete_msg),
//                    actionLabel = context.getString(R.string.action_undo)
//                ) { result ->
//                    if (result == SnackbarResult.ActionPerformed) {
//                        viewModel.insertOrUpdateTask(toDoTask)
//                    }
//                }
//            }
//
//            Action.DELETE_ALL -> {
//                viewModel.deleteAllTasks()
//                snackbarState.showSnackbar(
//                    R.string.delete_all_msg,
//                    actionLabel = context.getString(R.string.action_ok)
//                ) { result ->
//                    if (result == SnackbarResult.ActionPerformed) {
//                        snackbarState.dismiss()
//                    }
//                }
//            }
//
//            else -> Unit
//        }
//    }
//}
