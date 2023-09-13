package com.androidisland.todocompose.util

import androidx.compose.runtime.Immutable
import com.androidisland.todocompose.data.models.ToDoTask


@Immutable
data class ActionEvent(
    val action: Action,
    val toDoTask: ToDoTask?,
    private val time: Long = System.currentTimeMillis()
)