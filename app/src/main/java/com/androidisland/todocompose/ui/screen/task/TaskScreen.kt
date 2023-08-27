package com.androidisland.todocompose.ui.screen.task

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.androidisland.todocompose.data.models.Priority
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.util.Action


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    toDoTask: ToDoTask?,
    navigateToListScreen: (Action) -> Unit
) {
    Scaffold(topBar = {
        TaskAppBAr(toDoTask = toDoTask, navigateToListScreen = navigateToListScreen)
    }, content = {
        TaskContent(
            title = "title",
            onTitleChanged = {},
            description = "desc",
            onDescriptionChanged = {},
            priority = Priority.LOW,
            onPrioritySelected = {},
            it
        )
    })
}