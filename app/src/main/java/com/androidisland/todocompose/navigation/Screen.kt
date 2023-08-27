package com.androidisland.todocompose.navigation

import com.androidisland.todocompose.navigation.Screen.Task.Args.taskId

sealed class Screen(val route: String) {
    object TaskList : Screen("tasks")
    object Task : Screen("tasks/{$taskId}") {
        object Args {
            const val taskId = "taskId"
        }

        fun routeWith(taskId: Int) = "tasks/$taskId"
    }
}

