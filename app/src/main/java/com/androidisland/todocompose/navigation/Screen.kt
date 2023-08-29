package com.androidisland.todocompose.navigation

import androidx.navigation.NavDeepLink
import androidx.navigation.navDeepLink
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.util.Action
import com.google.gson.Gson

sealed class Screen(val route: String) {

    open val deepLinks = listOf<NavDeepLink>()
    protected val gson = Gson()

    object TaskList : Screen("tasks?${Args.action}={${Args.action}}&${Args.task}={${Args.task}}") {

        object Args {
            const val action = "action"
            const val task = "task"
        }

        override val deepLinks: List<NavDeepLink>
            get() = listOf(navDeepLink { uriPattern = "todo://tasks" })

        fun routeWith(
            action: Action?,
            toDoTask: ToDoTask?
        ) = StringBuilder("tasks").let { builder ->
            if (action != null && toDoTask != null) {
                builder.append(
                    "?${Args.action}=${action.name}&${Args.task}=${
                        gson.toJson(
                            toDoTask
                        )
                    }"
                )
            }
            builder.toString()
        }
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

