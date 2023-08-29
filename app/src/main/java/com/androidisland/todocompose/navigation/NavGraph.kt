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
import com.androidisland.todocompose.data.models.ToDoTaskNavType
import com.androidisland.todocompose.ext.getParcelableCompat
import com.androidisland.todocompose.ui.screen.list.ListScreen
import com.androidisland.todocompose.ui.screen.task.TaskScreen
import com.androidisland.todocompose.ui.viewmodel.SharedViewModel
import com.androidisland.todocompose.util.Action


@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    actions: Actions,
    sharedViewModel: SharedViewModel
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
    navigateToTask: (Int) -> Unit
) {
    composable(
        route = Screen.TaskList.route,
        arguments = listOf(
            navArgument(Screen.TaskList.Args.action) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(Screen.TaskList.Args.task) {
                type = ToDoTaskNavType()
                nullable = true
            }
        ),
//        enterTransition = { EnterTransition.None },
//        exitTransition = { ExitTransition.None },
//        popEnterTransition = { EnterTransition.None },
//        popExitTransition = { ExitTransition.None },
        deepLinks = Screen.TaskList.deepLinks
    ) { navBackStackEntry ->
        val action = navBackStackEntry.arguments?.getString(Screen.TaskList.Args.action)
            ?.let { Action.from(it) }
        val toDoTask =
            navBackStackEntry.arguments?.getParcelableCompat<ToDoTask>(Screen.TaskList.Args.task)
        ListScreen(
            action = action,
            toDoTask = toDoTask,
            viewModel = sharedViewModel,
            navigateToTaskScreen = navigateToTask
        )
    }
}

fun NavGraphBuilder.taskComposable(
    sharedViewModel: SharedViewModel,
    navigateToTaskList: (Action?, ToDoTask?) -> Unit
) {
    composable(
        route = Screen.Task.route,
        arguments = listOf(
            navArgument(Screen.Task.Args.taskId) {
                type = NavType.IntType
            }
        ),
//        enterTransition = { EnterTransition.None },
//        exitTransition = { ExitTransition.None },
//        popEnterTransition = { EnterTransition.None },
//        popExitTransition = { ExitTransition.None },
        deepLinks = Screen.Task.deepLinks
    ) { navBackStackEntry ->
        val task by produceState<ToDoTask?>(initialValue = null) {
            val taskId = navBackStackEntry.arguments!!.getInt(Screen.Task.Args.taskId)
            value = sharedViewModel.getTask(taskId)
        }
        TaskScreen(
            task,
            sharedViewModel,
            navigateToTaskList
        )
    }
}