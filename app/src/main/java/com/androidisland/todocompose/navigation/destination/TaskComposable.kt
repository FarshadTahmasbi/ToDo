package com.androidisland.todocompose.navigation.destination

import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.ui.screen.task.TaskScreen
import com.androidisland.todocompose.ui.viewmodel.SharedViewModel
import com.androidisland.todocompose.util.Action
import com.androidisland.todocompose.util.Constants.KEY_TASK_ID
import com.androidisland.todocompose.util.Constants.TASK_SCREEN

fun NavGraphBuilder.taskComposable(
    navigateToListScreen: (Action) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = TASK_SCREEN,
        arguments = listOf(
            navArgument(KEY_TASK_ID) {
                type = NavType.IntType
            },
//            navArgument(KEY_TASK) {
//                type = ToDoTaskNavType()
//                nullable = true
//            }
        )
    ) { navBackStackEntry ->
        val task by produceState<ToDoTask?>(initialValue = null) {
            val taskId = navBackStackEntry.arguments!!.getInt(KEY_TASK_ID)
            value = sharedViewModel.getTask(taskId)
        }
        TaskScreen(task, navigateToListScreen)
    }
}