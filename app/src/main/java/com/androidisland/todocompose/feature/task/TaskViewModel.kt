package com.androidisland.todocompose.feature.task

import androidx.lifecycle.SavedStateHandle
import com.androidisland.todocompose.arch.MviViewModel
import com.androidisland.todocompose.data.models.Priority
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.data.repository.ToDoRepository
import com.androidisland.todocompose.navigation.Screen
import com.androidisland.todocompose.thread.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext


@HiltViewModel
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dispatchers: CoroutineDispatchers,
    private val taskRepository: ToDoRepository
) : MviViewModel<TaskContract.State, TaskContract.Action, TaskContract.Effect>(savedStateHandle) {

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

    override suspend fun reduce(
        currentState: TaskContract.State,
        action: TaskContract.Action
    ): TaskContract.State {
        return when (action) {
            is TaskContract.Action.LoadTask -> onLoadTaskAction(currentState)
        }
    }

    private suspend fun onLoadTaskAction(currentState: TaskContract.State): TaskContract.State {
        return withContext(dispatchers.io) {
            val task = getTask()
            currentState.copy(task = task)
        }
    }

    fun loadTask() {
        dispatchAction(TaskContract.Action.LoadTask)
    }

//    private suspend fun onInsertOrUpdateTaskAction(
//        title: String,
//        desc: String,
//        priority: Priority
//    ) {
//        if (isValid(title, desc)) {
//
//        } else {
//            setEffect {
//                TaskContract.Effect.Snackbar(R.string.empty_fields_msg)
//            }
//        }
//    }

    private suspend fun getTask(): ToDoTask? {
        return if (isTaskIdValid) {
            taskRepository.getTask(taskId).firstOrNull()
        } else {
            null
        }
    }

    fun isValid(title: String, description: String): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }
}