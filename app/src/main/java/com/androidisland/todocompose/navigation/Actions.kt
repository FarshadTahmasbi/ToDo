package com.androidisland.todocompose.navigation

import androidx.navigation.NavHostController

class Actions(navHostController: NavHostController) {
    val navigateToTaskList: () -> Unit = {
        navHostController.navigate(Screen.TaskList.route) {
            popUpTo(Screen.TaskList.route) {
                inclusive = true
            }
        }
    }
    val navigateToTask: (taskId: Int) -> Unit = { taskId ->
        navHostController.navigate(Screen.Task.routeWith(taskId))
    }
}