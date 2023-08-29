package com.androidisland.todocompose.navigation

import androidx.compose.material3.SnackbarDuration
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
import com.androidisland.todocompose.util.Either


@Composable
fun SetUpToDoAppNavigation(
    navController: NavHostController,
    actions: Actions,
    sharedViewModel: SharedViewModel,
    snackbarState: SnackbarState,
) {
    val showSnackbar = { either: Either<String, Int> ->
        either.fold({ message ->
            snackbarState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }, { messageResId ->
            snackbarState.showSnackbar(
                message = messageResId,
                duration = SnackbarDuration.Short
            )
        })
    }

    NavHost(
        navController = navController, startDestination = Screen.TaskList.route
    ) {
        listComposable(sharedViewModel, actions.navigateToTask)
        taskComposable(sharedViewModel, actions.navigateToTaskList, showSnackbar)
    }
}

fun NavGraphBuilder.listComposable(
    sharedViewModel: SharedViewModel,
    navigateToTask: (Int) -> Unit,
) {
    composable(
        route = Screen.TaskList.route,
        deepLinks = Screen.TaskList.deepLinks
    ) {
        ListScreen(
            sharedViewModel = sharedViewModel,
            navigateToTaskScreen = navigateToTask,
        )
    }
}

fun NavGraphBuilder.taskComposable(
    sharedViewModel: SharedViewModel,
    navigateToTaskList: () -> Unit,
    showSnackbar: (Either<String, Int>) -> Unit
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
        TaskScreen(sharedViewModel, showSnackbar, task, navigateToTaskList)
    }
}