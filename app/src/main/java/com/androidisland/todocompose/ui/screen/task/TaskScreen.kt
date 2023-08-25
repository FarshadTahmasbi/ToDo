package com.androidisland.todocompose.ui.screen.task

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.util.Action


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    toDoTask: ToDoTask?,
    navigateToListScreen: (Action) -> Unit
) {
    Scaffold(topBar = {
        TaskAppBAr(toDoTask = toDoTask, navigateToListScreen = navigateToListScreen)
    }, content = {
        it.toString()
    })
}