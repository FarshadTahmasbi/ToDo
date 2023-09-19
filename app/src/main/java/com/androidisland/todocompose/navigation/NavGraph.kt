package com.androidisland.todocompose.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
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
    screenComposable(Screen.Task) {
        TaskScreen(
            hiltViewModel(),
            sharedViewModel,
            navigateToTaskList
        )
    }
}