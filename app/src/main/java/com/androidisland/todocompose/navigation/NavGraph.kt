package com.androidisland.todocompose.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.androidisland.todocompose.data.models.Priority
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.ext.screenComposable
import com.androidisland.todocompose.ui.screen.list.ListScreen
import com.androidisland.todocompose.ui.screen.splash.SplashScreen
import com.androidisland.todocompose.ui.screen.task.TaskScreen
import com.androidisland.todocompose.ui.viewmodel.SharedViewModel


@Composable
fun SetUpNavGraph(
    intent: Intent,
    navController: NavHostController,
    actions: Actions,
    sharedViewModel: SharedViewModel
) {
    val hasSplash =
        intent.data == null || Screen.Splash.containsDeepLink(intent.data.toString())
    val startDestination = if (hasSplash) Screen.Splash.route else Screen.TaskList.route
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        if (hasSplash) {
            splashComposable(actions.navigateToTaskList)
        }
        listComposable(sharedViewModel, actions.navigateToTask)
        taskComposable(sharedViewModel, actions.navigateToTaskList)
    }
}

fun NavGraphBuilder.splashComposable(
    navigateToTaskList: () -> Unit
) {
    screenComposable(Screen.Splash) {
        SplashScreen(navigateToTaskList = navigateToTaskList)
    }
}

fun NavGraphBuilder.listComposable(
    sharedViewModel: SharedViewModel,
    navigateToTask: (Int) -> Unit
) {
    screenComposable(Screen.TaskList) {
        ListScreen(
            sharedViewModel = sharedViewModel,
            navigateToTaskScreen = navigateToTask
        )
    }
}

fun NavGraphBuilder.taskComposable(
    sharedViewModel: SharedViewModel,
    navigateToTaskList: () -> Unit
) {
    screenComposable(Screen.Task) { navBackStackEntry ->
        val taskId = navBackStackEntry.arguments!!.getInt(Screen.Task.Args.taskId)
        //Here If I get a valid task id, I pass a fake tak as initial value until the real one is loaded
        //In this way, compose picks the right toolbar at first composition!
        val initialTask = if (taskId > 0) ToDoTask(0, "", "", Priority.LOW) else null
        val task by produceState(initialValue = initialTask) {
            value = sharedViewModel.getTask(taskId)
        }
        TaskScreen(
            task,
            hiltViewModel(),
            sharedViewModel,
            navigateToTaskList
        )
    }
}