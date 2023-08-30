package com.androidisland.todocompose.navigation

import androidx.navigation.NavDeepLink
import androidx.navigation.navDeepLink
import com.google.gson.Gson

sealed class Screen(val route: String) {

    open val deepLinks = listOf<NavDeepLink>()
    protected val gson = Gson()

    object TaskList : Screen("tasks") {

        override val deepLinks: List<NavDeepLink>
            get() = listOf(navDeepLink { uriPattern = "todo://tasks" })
    }

    object Task : Screen("tasks/{${Args.taskId}}") {

        object Args {
            const val taskId = "taskId"
        }

        override val deepLinks: List<NavDeepLink>
            get() = listOf(navDeepLink { uriPattern = "todo://tasks/{${Args.taskId}}" })

        fun routeWith(taskId: Int) = "tasks/$taskId"
    }
}

