package com.androidisland.todocompose.ui.screen.task

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.androidisland.todocompose.R
import com.androidisland.todocompose.data.models.Priority
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.ui.common.CustomSnackbarHost
import com.androidisland.todocompose.ui.common.rememberSnackbarState
import com.androidisland.todocompose.ui.viewmodel.SharedViewModel
import com.androidisland.todocompose.util.Action
import com.androidisland.todocompose.util.Either


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    toDoTask: ToDoTask?,
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action?, ToDoTask?) -> Unit
) {
    val snackbarAppState = rememberSnackbarState()
    var title by remember(toDoTask) {
        mutableStateOf(toDoTask?.title.orEmpty())
    }

    var description by remember(toDoTask) {
        mutableStateOf(toDoTask?.description.orEmpty())
    }

    var priority by remember(toDoTask) {
        mutableStateOf(toDoTask?.priority ?: Priority.LOW)
    }

    val onActionClicked: (Action) -> Unit = remember(toDoTask) {
        { action: Action ->
            if (action.isEditMode() && sharedViewModel.isValid(title, description).not()) {
                snackbarAppState.showSnackbar(message = Either.Right(R.string.empty_fields_msg))
            } else {
                val task = when (action) {
                    Action.ADD -> ToDoTask(0, title, description, priority)
                    Action.UPDATE -> toDoTask!!.copy(
                        title = title,
                        description = description,
                        priority = priority
                    )

                    Action.DELETE -> toDoTask
                    else -> null
                }
                navigateToListScreen(action, task)
            }
        }
    }

    Scaffold(topBar = {
        TaskAppBar(
            toDoTask = toDoTask,
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
