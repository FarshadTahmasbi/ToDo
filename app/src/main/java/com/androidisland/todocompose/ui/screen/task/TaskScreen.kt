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
import com.androidisland.todocompose.ui.viewmodel.SharedViewModel
import com.androidisland.todocompose.util.Action
import com.androidisland.todocompose.util.Either


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    sharedViewModel: SharedViewModel,
    showSnackbar: (Either<String, Int>) -> Unit,
    toDoTask: ToDoTask?,
    navigateToListScreen: () -> Unit
) {
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
                showSnackbar(Either.Right(R.string.empty_fields_msg))
            } else {
                when (action) {
                    Action.ADD -> {
                        sharedViewModel.addTask(ToDoTask(0, title, description, priority))
                        showSnackbar(Either.Right(R.string.task_add_msg))
                    }

                    Action.UPDATE -> {
                        sharedViewModel.updateTask(
                            toDoTask!!.copy(
                                title = title, description = description, priority = priority
                            )
                        )
                        showSnackbar(Either.Right(R.string.task_update_msg))
                    }

                    Action.DELETE -> {
                        sharedViewModel.deleteTask(toDoTask!!)
                        showSnackbar(Either.Right(R.string.task_delete_msg))
                    }

                    else -> Unit
                }
                navigateToListScreen()
            }
        }
    }


    Scaffold(topBar = {
        TaskAppBar(toDoTask = toDoTask, onActionClicked = onActionClicked)
    }, content = { padding ->
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