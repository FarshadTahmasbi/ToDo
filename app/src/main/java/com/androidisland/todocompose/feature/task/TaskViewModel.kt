package com.androidisland.todocompose.feature.task

import androidx.lifecycle.SavedStateHandle
import com.androidisland.todocompose.R
import com.androidisland.todocompose.arch.CommonEffect
import com.androidisland.todocompose.arch.MviViewModel
import com.androidisland.todocompose.common.resource.StringResProvider
import com.androidisland.todocompose.data.models.TaskActionEvent
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.data.repository.ToDoRepository
import com.androidisland.todocompose.enums.Priority
import com.androidisland.todocompose.enums.TaskAction
import com.androidisland.todocompose.navigation.Screen
import com.androidisland.todocompose.thread.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext


@HiltViewModel
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dispatchers: CoroutineDispatchers,
    private val taskActionEventChannel: Channel<TaskActionEvent>,
    private val stringResProvider: StringResProvider,
    private val taskRepository: ToDoRepository
) : MviViewModel<TaskContract.State, TaskContract.Action, CommonEffect>(savedStateHandle) {

    private val taskId: Int
        get() = requireNotNull(savedStateHandle[Screen.Args.taskId])
    private val isTaskIdValid: Boolean
        get() = taskId > 0

    override val initialState: TaskContract.State
        get() = TaskContract.State(id = taskId)

    override suspend fun handleAction(
        currentState: TaskContract.State,
        action: TaskContract.Action
    ) {
        when (action) {
            is TaskContract.Action.LoadTask -> onLoadTaskAction(currentState)
            is TaskContract.Action.NavigateToTaskList -> dispatchEffect {
                TaskContract.Effect.NavigateToTaskList
            }

            is TaskContract.Action.OnTaskTitleChange -> setState(currentState.copy(title = action.value))
            is TaskContract.Action.OnTaskDescriptionChange -> setState(currentState.copy(description = action.value))
            is TaskContract.Action.OnTaskPriorityChange -> setState(currentState.copy(priority = action.value))
            is TaskContract.Action.OnAddOrInsertClick -> onAddOrInsertClickAction()
            is TaskContract.Action.OnDeleteClick -> onDeleteClickAction()
        }
    }

    private suspend fun onLoadTaskAction(currentState: TaskContract.State) {
        return withContext(dispatchers.io) {
            val task = getTask()
            setState(
                currentState.copy(
                    id = task?.id ?: 0,
                    title = task?.title.orEmpty(),
                    description = task?.description.orEmpty(),
                    priority = task?.priority ?: Priority.LOW
                )
            )
        }
    }

    private fun onAddOrInsertClickAction() {
        val task = currentState.toTask()
        if (taskRepository.isValidTask(task)) {
            taskActionEventChannel.trySend(TaskActionEvent(TaskAction.INSERT_OR_UPDATE, task))
            navigateToTaskList()
        } else {
            dispatchEffect {
                CommonEffect.ShowSnackBar(
                    stringResProvider[R.string.empty_fields_msg]
                )
            }
        }
    }

    private fun onDeleteClickAction() {
        val task = currentState.toTask()
        taskActionEventChannel.trySend(TaskActionEvent(TaskAction.DELETE, task))
        navigateToTaskList()
    }

    fun loadTask() {
        dispatchAction(TaskContract.Action.LoadTask)
    }

    fun onTaskTitleChange(title: String) {
        dispatchAction(TaskContract.Action.OnTaskTitleChange(title))
    }

    fun onTaskDescriptionChange(description: String) {
        dispatchAction(TaskContract.Action.OnTaskDescriptionChange(description))
    }

    fun onTaskPriorityChange(priority: Priority) {
        dispatchAction(TaskContract.Action.OnTaskPriorityChange(priority))
    }

    fun navigateToTaskList() {
        dispatchEffect {
            TaskContract.Effect.NavigateToTaskList
        }
    }

    private suspend fun getTask(): ToDoTask? {
        return if (isTaskIdValid) {
            taskRepository.getTask(taskId).firstOrNull()
        } else {
            null
        }
    }

    fun onActionClick(taskAction: TaskAction) {
        when (taskAction) {
            TaskAction.INSERT_OR_UPDATE -> dispatchAction(TaskContract.Action.OnAddOrInsertClick)
            TaskAction.DELETE -> dispatchAction(TaskContract.Action.OnDeleteClick)
        }
    }
}