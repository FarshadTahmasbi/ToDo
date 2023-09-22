package com.androidisland.todocompose.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.androidisland.todocompose.common.SharedViewModel
import com.androidisland.todocompose.ext.screenComposable
import com.androidisland.todocompose.feature.splash.ui.SplashScreen
import com.androidisland.todocompose.feature.task.ui.TaskScreen
import com.androidisland.todocompose.feature.tasklist.ui.TaskListScreen


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
        taskComposable(actions.navigateToTaskList)
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
        TaskListScreen(
            viewModel = sharedViewModel,
            navigateToTaskScreen = navigateToTask
        )
    }
}

fun NavGraphBuilder.taskComposable(
    navigateToTaskList: () -> Unit
) {
    screenComposable(Screen.Task) {
        TaskScreen(
            hiltViewModel(),
            navigateToTaskList
        )
    }
}