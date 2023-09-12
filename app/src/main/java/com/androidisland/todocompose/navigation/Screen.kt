package com.androidisland.todocompose.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

sealed class Screen(val route: String) {

    open val deepLinks = listOf<NavDeepLink>()
    open val arguments: List<NamedNavArgument> = emptyList()
    open val transitions: NavTransition = NavTransition.None

    fun containsDeepLink(deepLink: String) =
        deepLinks.mapNotNull { it.uriPattern }.contains(deepLink)

    object Splash : Screen("splash") {
        override val deepLinks: List<NavDeepLink>
            get() = listOf(navDeepLink { uriPattern = "todo://" })
    }

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

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(Args.taskId) {
                    type = NavType.IntType
                }
            )

        fun routeWith(taskId: Int) = "tasks/$taskId"
    }
}

