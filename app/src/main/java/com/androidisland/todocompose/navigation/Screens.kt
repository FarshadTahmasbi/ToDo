package com.androidisland.todocompose.navigation

import androidx.navigation.NavHostController
import com.androidisland.todocompose.util.Action
import com.androidisland.todocompose.util.Constants.LIST_SCREEN


class Screens(navController: NavHostController) {
    val list: (Action) -> Unit = { action ->
        navController.navigate("list/${action.name}") {
            popUpTo(LIST_SCREEN) {
                inclusive = true
            }
        }
    }
    val task: (Int) -> Unit = { taskId ->
//        val task = ToDoTask(1, "title", "desc", Priority.LOW)
//        val json = Uri.encode(Gson().toJson(task))
//        navController.navigate("task/$taskId?task=$json")
        navController.navigate("task/$taskId")
    }
}