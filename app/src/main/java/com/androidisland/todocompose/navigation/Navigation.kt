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
import androidx.navigation.navDeepLink
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.ui.screen.list.ListScreen
import com.androidisland.todocompose.ui.screen.task.TaskScreen
import com.androidisland.todocompose.ui.viewmodel.SharedViewModel
import com.androidisland.todocompose.util.Constants


@Composable
fun SetUpToDoAppNavigation(
    navController: NavHostController, actions: Actions, sharedViewModel: SharedViewModel
) {
    NavHost(
        navController = navController, startDestination = Screen.TaskList.route
    ) {
        listComposable(sharedViewModel, actions.navigateToTask)
        taskComposable(sharedViewModel, actions.navigateToTaskList)
    }
}

fun NavGraphBuilder.listComposable(
    sharedViewModel: SharedViewModel,
    navigateToTask: (Int) -> Unit,
) {
    composable(route = Screen.TaskList.route) {
        ListScreen(
            sharedViewModel = sharedViewModel, navigateToTaskScreen = navigateToTask
        )
    }
}

fun NavGraphBuilder.taskComposable(
    sharedViewModel: SharedViewModel,
    navigateToTaskList: () -> Unit,
) {
    composable(
        route = Screen.Task.route, arguments = listOf(
            navArgument(Screen.Task.Args.taskId) {
                type = NavType.IntType
            },
//            navArgument(KEY_TASK) {
//                type = ToDoTaskNavType()
//                nullable = true
//            }
        ),
        //TODO move to screen
        deepLinks = listOf(navDeepLink { uriPattern = "${Constants.DEEP_LINK_URI}/{taskId}" })
    ) { navBackStackEntry ->
        val task by produceState<ToDoTask?>(initialValue = null) {
            val taskId = navBackStackEntry.arguments!!.getInt(Screen.Task.Args.taskId)
            value = sharedViewModel.getTask(taskId)
        }
        TaskScreen(task, sharedViewModel, navigateToTaskList)
    }
}