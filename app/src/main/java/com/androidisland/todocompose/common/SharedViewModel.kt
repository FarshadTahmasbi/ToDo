package com.androidisland.todocompose.common

import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.androidisland.todocompose.R
import com.androidisland.todocompose.arch.CommonEffect
import com.androidisland.todocompose.arch.MviViewModel
import com.androidisland.todocompose.common.resource.StringResProvider
import com.androidisland.todocompose.data.models.TaskActionEvent
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.data.repository.DataStoreRepository
import com.androidisland.todocompose.data.repository.ToDoRepository
import com.androidisland.todocompose.enums.PrioritySort
import com.androidisland.todocompose.enums.TaskAction
import com.androidisland.todocompose.feature.tasklist.TaskListContract
import com.androidisland.todocompose.thread.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SharedViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskActionEventFlow: Flow<TaskActionEvent>,
    private val stringResProvider: StringResProvider,
    private val dispatchers: CoroutineDispatchers,
    private val toDoRepository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository
) : MviViewModel<TaskListContract.State, TaskListContract.Action, CommonEffect>(
    savedStateHandle
) {

    override val initialState: TaskListContract.State
        get() = TaskListContract.State(isLoading = true)

    private val searchQuery: MutableStateFlow<String?> = MutableStateFlow(null)
    private val sortState = dataStoreRepository.sortState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null
        )

    private val delay = 0L

    init {
        collectTaskActions()
        collectSearchAndSortCombination()
    }

    private fun collectTaskActions() {
        taskActionEventFlow.onEach {
            val (action, task) = it
            when (action) {
                TaskAction.INSERT_OR_UPDATE -> insertOrUpdateTask(task)
                TaskAction.DELETE -> deleteTask(task)
            }
        }.launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun collectSearchAndSortCombination() {
        searchQuery.combine(sortState.mapNotNull { it }) { query, sort ->
            query to sort
        }.distinctUntilChanged { old, new ->
            //If the query is not null, ignore the sort state changes
            old.first != null && old.first == new.first
        }.flatMapLatest { querySortPair ->
            val (query, sort) = querySortPair
            val tasksFlow = if (query == null) {
                toDoRepository.getTasks(sort)
            } else {
                toDoRepository.queryTasks(query)
            }.map { tasks ->
                TaskListContract.State(
                    tasks = tasks,
                    query = query,
                    sort = sort
                )
            }
            flowOf(
                flowOf(TaskListContract.State(isLoading = true)),
                tasksFlow
            ).flatMapConcat { it }
        }.catch {
            emit(
                TaskListContract.State(errorMessage = stringResProvider[R.string.message_general_error])
            )
        }.onEach {
            setState(it)
        }.launchIn(viewModelScope)
    }

    override suspend fun handleAction(
        currentState: TaskListContract.State,
        action: TaskListContract.Action
    ) {
        when (action) {
            is TaskListContract.Action.NavigateToNewTask -> onNavigateToNewTask()
            is TaskListContract.Action.InsertOrUpdateTask -> onInsertOrUpdateTask(action.task)
            is TaskListContract.Action.DeleteTask -> onDeleteTaskAction(action.task)
            is TaskListContract.Action.UndoDeleteTask -> onUndoDeleteTaskAction(action.task)
            is TaskListContract.Action.ShowTaskDetail -> onShowTaskDetailAction(action.taskId)
            is TaskListContract.Action.QueryTasks -> onQueryTasksAction(action.query)
            is TaskListContract.Action.SortTasks -> onSortTasksAction(action.priority)
            is TaskListContract.Action.DeleteAllTasks -> onDeleteAllTasksAction()
            else -> Unit
        }
    }

    private fun onNavigateToNewTask() {
        dispatchEffect {
            TaskListContract.Effect.NavigateToTask(0)
        }
    }

    private fun onInsertOrUpdateTask(task: ToDoTask) {
        viewModelScope.launch(dispatchers.io) {
            if (toDoRepository.isValidTask(task)) {
                val taskAlreadyExists = task.id > 0
                if (taskAlreadyExists) {
                    toDoRepository.updateTask(task)
                } else {
                    toDoRepository.insertTask(task)
                }
                dispatchEffect {
                    CommonEffect.ShowSnackBar(
                        stringResProvider[if (taskAlreadyExists) R.string.task_update_msg
                        else R.string.task_insert_msg]
                    )
                }
            } else {
                dispatchEffect {
                    CommonEffect.ShowSnackBar(
                        stringResProvider[R.string.empty_fields_msg]
                    )
                }
            }
        }
    }

    private fun onDeleteTaskAction(task: ToDoTask) {
        viewModelScope.launch(dispatchers.io) {
            toDoRepository.deleteTask(task)
            dispatchEffect {
                CommonEffect.ShowSnackBarWithAction(
                    message = stringResProvider[R.string.task_delete_msg],
                    action = stringResProvider[R.string.action_undo],
                    onResult = { result ->
                        if (result == SnackbarResult.ActionPerformed) {
                            dispatchAction(TaskListContract.Action.UndoDeleteTask(task))
                        }
                    }
                )
            }
        }
    }

    private fun onUndoDeleteTaskAction(task: ToDoTask) {
        viewModelScope.launch(dispatchers.io) {
            toDoRepository.insertTask(task)
        }
    }

    private fun onShowTaskDetailAction(taskId: Int) {
        dispatchEffect {
            TaskListContract.Effect.NavigateToTask(taskId)
        }
    }

    private fun onQueryTasksAction(query: String?) {
        searchQuery.value = query
    }

    private fun onSortTasksAction(sort: PrioritySort) {
        viewModelScope.launch(dispatchers.io) {
            dataStoreRepository.persistSortState(sort)
        }
    }

    private fun onDeleteAllTasksAction() {
        viewModelScope.launch(dispatchers.io) {
            toDoRepository.deleteTasks()
        }
    }

    fun queryTasks(query: String?) {
        dispatchAction(TaskListContract.Action.QueryTasks(query))
    }

    fun insertOrUpdateTask(task: ToDoTask) {
        dispatchAction(TaskListContract.Action.InsertOrUpdateTask(task))
    }

    fun deleteTask(toDoTask: ToDoTask) {
        dispatchAction(TaskListContract.Action.DeleteTask(toDoTask))
    }

    fun deleteAllTasks() {
        dispatchAction(TaskListContract.Action.DeleteAllTasks)
    }

    fun persistSortState(sort: PrioritySort) {
        dispatchAction(TaskListContract.Action.SortTasks(sort))
    }
}