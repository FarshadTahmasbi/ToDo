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
        get() = TaskContract.State(
            //I did this to have a better user experience and prevent toolbar change
            //when task is loaded
            if (isTaskIdValid) ToDoTask(0, "", "", Priority.LOW)
            else null
        )

    override suspend fun handleAction(
        currentState: TaskContract.State,
        action: TaskContract.Action
    ) {
        when (action) {
            is TaskContract.Action.LoadTask -> onLoadTaskAction(currentState)
            is TaskContract.Action.NavigateToTaskList -> dispatchEffect {
                TaskContract.Effect.NavigateToTaskList
            }

            else -> Unit
        }
    }

    private suspend fun onLoadTaskAction(currentState: TaskContract.State) {
        return withContext(dispatchers.io) {
            val task = getTask()
            setState(currentState.copy(task = task))
        }
    }

    fun loadTask() {
        dispatchAction(TaskContract.Action.LoadTask)
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

    fun dispatchTaskAction(action: TaskAction, task: ToDoTask) {
        when (action) {
            TaskAction.INSERT_OR_UPDATE -> {
                if (taskRepository.isValidTask(task)) {
                    taskActionEventChannel.trySend(TaskActionEvent(action, task))
                    navigateToTaskList()
                } else {
                    dispatchEffect {
                        CommonEffect.ShowSnackBar(
                            stringResProvider[R.string.empty_fields_msg]
                        )
                    }
                }
            }

            TaskAction.DELETE -> {
                taskActionEventChannel.trySend(TaskActionEvent(action, task))
                navigateToTaskList()
            }
        }
    }
}