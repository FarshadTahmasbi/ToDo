package com.androidisland.todocompose.feature.task

import com.androidisland.todocompose.arch.CommonEffect
import com.androidisland.todocompose.arch.MviAction
import com.androidisland.todocompose.arch.MviState
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.enums.Priority


class TaskContract {
    data class State(
        val originTask: ToDoTask?,
        val modifiedTask: ToDoTask
    ) : MviState

    sealed class Action : MviAction {
        object LoadTask : Action()
        data class OnTaskTitleChange(val value: String) : Action()
        data class OnTaskDescriptionChange(val value: String) : Action()
        data class OnTaskPriorityChange(val value: Priority) : Action()

        object OnAddOrInsertClick : Action()
        object OnDeleteClick : Action()
        object NavigateToTaskList : Action()
    }

    sealed class Effect : CommonEffect.FeatureEffect() {
        object NavigateToTaskList : Effect()
    }
}