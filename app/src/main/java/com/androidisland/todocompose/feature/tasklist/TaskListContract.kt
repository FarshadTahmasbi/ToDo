package com.androidisland.todocompose.feature.tasklist

import com.androidisland.todocompose.arch.CommonEffect
import com.androidisland.todocompose.arch.MviAction
import com.androidisland.todocompose.arch.MviState
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.enums.PrioritySort


class TaskListContract {

    data class State(
        val isLoading: Boolean = false,
        val tasks: List<ToDoTask> = emptyList(),
        val query: String? = null,
        val sort: PrioritySort = PrioritySort.DEFAULT,
        val errorMessage: String? = null
    ) : MviState

    sealed class Action : MviAction {
        object NavigateToNewTask : Action()
        data class InsertOrUpdateTask(val task: ToDoTask) : Action()
        data class DeleteTask(val task: ToDoTask) : Action()
        data class UndoDeleteTask(val task: ToDoTask) : Action()
        data class ShowTaskDetail(val taskId: Int) : Action()
        data class QueryTasks(val query: String?) : Action()
        data class SortTasks(val priority: PrioritySort) : Action()
        object DeleteAllTasks : Action()
    }

    sealed class Effect : CommonEffect.FeatureEffect() {
        data class NavigateToTask(val id: Int) : Effect()
    }
}