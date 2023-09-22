package com.androidisland.todocompose.data.models

import com.androidisland.todocompose.enums.TaskAction


data class TaskActionEvent(val action: TaskAction, val task: ToDoTask)