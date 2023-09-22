package com.androidisland.todocompose.feature.task.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.androidisland.todocompose.arch.CollectEffects
import com.androidisland.todocompose.arch.CommonEffect
import com.androidisland.todocompose.common.ui.CustomSnackbarHost
import com.androidisland.todocompose.common.ui.SnackbarState
import com.androidisland.todocompose.common.ui.rememberSnackbarState
import com.androidisland.todocompose.feature.task.TaskContract
import com.androidisland.todocompose.feature.task.TaskViewModel
import kotlinx.coroutines.flow.Flow


@Composable
fun TaskScreen(
    taskViewModel: TaskViewModel,
    navigateToListScreen: () -> Unit
) {
    val state by taskViewModel.state.collectAsState()
    LaunchedEffect(key1 = Unit) {
        taskViewModel.loadTask()
    }

    val snackbarAppState = rememberSnackbarState()

    HandleEffects(
        effects = taskViewModel.effect,
        snackbarAppState = snackbarAppState,
        navigateToTaskListScreen = navigateToListScreen
    )


//    var title by remember(state) {
//        mutableStateOf(state.task?.title.orEmpty())
//    }
//
//    var description by remember(state) {
//        mutableStateOf(state.task?.description.orEmpty())
//    }
//
//    var priority by remember(state) {
//        mutableStateOf(state.task?.priority ?: Priority.LOW)
//    }


    Scaffold(topBar = {
        TaskAppBar(
            task = state.toTask(),
            onCloseClicked = taskViewModel::navigateToTaskList,
            onActionClicked = taskViewModel::onActionClick
        )
    }, snackbarHost = { CustomSnackbarHost(hostState = snackbarAppState.hostState) },
        content = { padding ->
            TaskContent(
                title = state.title,
                onTitleChanged = taskViewModel::onTaskTitleChange,
                description = state.description,
                onDescriptionChanged = taskViewModel::onTaskDescriptionChange,
                priority = state.priority,
                onPrioritySelected = taskViewModel::onTaskPriorityChange,
                padding
            )
        })
}

@Composable
fun HandleEffects(
    effects: Flow<CommonEffect>,
    snackbarAppState: SnackbarState,
    navigateToTaskListScreen: () -> Unit
) {
    CollectEffects(effect = effects) { effect ->
        when (effect) {
            is CommonEffect.ShowSnackBar -> snackbarAppState.showSnackbar(
                message = effect.message,
                duration = effect.duration
            )

            is TaskContract.Effect.NavigateToTaskList -> navigateToTaskListScreen()
            else -> Unit
        }
    }
}
