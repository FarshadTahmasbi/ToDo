package com.androidisland.todocompose.ui.screen.task

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.androidisland.todocompose.R
import com.androidisland.todocompose.data.models.Priority
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.task.TaskViewModel
import com.androidisland.todocompose.ui.common.CustomSnackbarHost
import com.androidisland.todocompose.ui.common.rememberSnackbarState
import com.androidisland.todocompose.ui.viewmodel.SharedViewModel
import com.androidisland.todocompose.util.Action
import com.androidisland.todocompose.util.Either


@Composable
fun TaskScreen(
    taskViewModel: TaskViewModel,
    sharedViewModel: SharedViewModel,
    navigateToListScreen: () -> Unit
) {
    val state by taskViewModel.state.collectAsState()
    LaunchedEffect(key1 = Unit) {
        taskViewModel.loadTask()
    }

    val snackbarAppState = rememberSnackbarState()

    var title by remember(state) {
        mutableStateOf(state.task?.title.orEmpty())
    }

    var description by remember(state) {
        mutableStateOf(state.task?.description.orEmpty())
    }

    var priority by remember(state) {
        mutableStateOf(state.task?.priority ?: Priority.LOW)
    }

    val onActionClicked: (Action) -> Unit = remember(state) {
        { action: Action ->
            if (action.isEditMode() && taskViewModel.isValid(title, description).not()) {
                snackbarAppState.showSnackbar(message = Either.Right(R.string.empty_fields_msg))
            } else {
                val currentTask = state.task
                val task = when (action) {
                    Action.ADD -> ToDoTask(0, title, description, priority)
                    Action.UPDATE -> currentTask!!.copy(
                        title = title,
                        description = description,
                        priority = priority
                    )

                    Action.DELETE -> currentTask
                    else -> null
                }
                if (task != null) {
                    sharedViewModel.sendActionEvent(action, task)
                }
                navigateToListScreen()
            }
        }
    }

    Scaffold(topBar = {
        TaskAppBar(
            toDoTask = state.task,
            onActionClicked = onActionClicked
        )
    }, snackbarHost = { CustomSnackbarHost(hostState = snackbarAppState.hostState) },
        content = { padding ->
            TaskContent(title = title, onTitleChanged = {
                title = it
            }, description = description, onDescriptionChanged = {
                description = it
            }, priority = priority, onPrioritySelected = {
                priority = it
            }, padding
            )
        })
}
