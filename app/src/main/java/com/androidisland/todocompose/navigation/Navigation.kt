package com.androidisland.todocompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.ui.screen.list.ListScreen
import com.androidisland.todocompose.ui.screen.task.TaskScreen
import com.androidisland.todocompose.ui.viewmodel.SharedViewModel


@Composable
fun SetUpToDoAppNavigation(
    navController: NavHostController,
    actions: Actions,
    sharedViewModel: SharedViewModel,
    snackbarState: SnackbarAppState,
) {
    NavHost(
        navController = navController, startDestination = Screen.TaskList.route
    ) {
        listComposable(sharedViewModel, actions.navigateToTask, snackbarState)
        taskComposable(sharedViewModel, actions.navigateToTaskList, snackbarState)
    }
}

fun NavGraphBuilder.listComposable(
    sharedViewModel: SharedViewModel,
    navigateToTask: (Int) -> Unit,
    snackbarAppState: SnackbarAppState
) {
    composable(
        route = Screen.TaskList.route,
        deepLinks = Screen.TaskList.deepLinks
    ) {
        ListScreen(
            sharedViewModel = sharedViewModel,
            navigateToTaskScreen = navigateToTask,
            snackbarAppState = snackbarAppState
        )
    }
}

fun NavGraphBuilder.taskComposable(
    sharedViewModel: SharedViewModel,
    navigateToTaskList: () -> Unit,
    snackbarState: SnackbarAppState
) {
    composable(
        route = Screen.Task.route,
        arguments = listOf(
            navArgument(Screen.Task.Args.taskId) {
                type = NavType.IntType
            },
//            navArgument(KEY_TASK) {
//                type = ToDoTaskNavType()
//                nullable = true
//            }
        ),
        //TODO move to screen
        deepLinks = Screen.Task.deepLinks
    ) { navBackStackEntry ->
        val task by produceState<ToDoTask?>(initialValue = null) {
            val taskId = navBackStackEntry.arguments!!.getInt(Screen.Task.Args.taskId)
            value = sharedViewModel.getTask(taskId)
        }
        TaskScreen(
            sharedViewModel,
            snackbarState,
            task,
            navigateToTaskList
        )
    }
}