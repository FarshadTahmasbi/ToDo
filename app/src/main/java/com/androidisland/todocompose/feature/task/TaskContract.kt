package com.androidisland.todocompose.feature.task

import com.androidisland.todocompose.arch.CommonEffect
import com.androidisland.todocompose.arch.MviAction
import com.androidisland.todocompose.arch.MviState
import com.androidisland.todocompose.data.models.ToDoTask


class TaskContract {
    data class State(val task: ToDoTask?) : MviState
    sealed class Action : MviAction {
        object LoadTask : Action()
        object NavigateToTaskList : Action()
    }

    sealed class Effect : CommonEffect.FeatureEffect() {
        object NavigateToTaskList : Effect()
    }
}