package com.androidisland.todocompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.androidisland.todocompose.navigation.destination.listComposable
import com.androidisland.todocompose.navigation.destination.taskComposable
import com.androidisland.todocompose.util.Constants.LIST_SCREEN


@Composable
fun SetupNavigation(navController: NavHostController) {
    val screen = remember(navController) {
        Screens(navController)
    }
    NavHost(
        navController = navController,
        startDestination = LIST_SCREEN
    ) {
        listComposable(screen.task)
        taskComposable(screen.list)
    }
}