package com.androidisland.todocompose.navigation

import androidx.navigation.NavHostController
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.util.Action

class Actions(navHostController: NavHostController) {
    val navigateToTaskList: (Action?, ToDoTask?) -> Unit = { action, task ->
        navHostController.navigate(Screen.TaskList.routeWith(action, task)) {
            popUpTo(Screen.TaskList.route) {
                inclusive = true
            }
        }
    }
    val navigateToTask: (taskId: Int) -> Unit = { taskId ->
        navHostController.navigate(Screen.Task.routeWith(taskId))
    }
}