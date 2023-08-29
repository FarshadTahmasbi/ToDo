package com.androidisland.todocompose.navigation

import androidx.navigation.NavDeepLink
import androidx.navigation.navDeepLink
import com.androidisland.todocompose.navigation.Screen.Task.Args.taskId

sealed class Screen(val route: String) {

    open val deepLinks = listOf<NavDeepLink>()

    object TaskList : Screen("tasks") {

        override val deepLinks: List<NavDeepLink>
            get() = listOf(navDeepLink { uriPattern = "todo://tasks" })
    }

    object Task : Screen("tasks/{$taskId}") {

        object Args {
            const val taskId = "taskId"
        }

        override val deepLinks: List<NavDeepLink>
            get() = listOf(navDeepLink { uriPattern = "todo://tasks/{$taskId}" })

        fun routeWith(taskId: Int) = "tasks/$taskId"
    }
}

