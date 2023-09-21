package com.androidisland.todocompose.feature.task

import androidx.annotation.StringRes
import com.androidisland.todocompose.data.models.ToDoTask


class TaskContract {
    data class State(val task: ToDoTask?) :
        com.androidisland.todocompose.arch.State
    sealed class Action : com.androidisland.todocompose.arch.Action {
        object LoadTask : Action()
//        data class InsertOrUpdateTask(
//            val title: String,
//            val desc: String,
//            val priority: Priority
//        ) : Action()
    }

    sealed class Effect : com.androidisland.todocompose.arch.Effect {
        data class Snackbar(@StringRes val message: Int) : Effect()
    }
}