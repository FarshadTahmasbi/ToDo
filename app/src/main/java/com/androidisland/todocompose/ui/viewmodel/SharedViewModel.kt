package com.androidisland.todocompose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidisland.todocompose.data.models.Priority
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.data.repository.DataStoreRepository
import com.androidisland.todocompose.data.repository.ToDoRepository
import com.androidisland.todocompose.thread.CoroutineDispatchers
import com.androidisland.todocompose.util.Action
import com.androidisland.todocompose.util.ActionEvent
import com.androidisland.todocompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    val actionEvent: Flow<ActionEvent?>,
    private val actionEventChannel: Channel<ActionEvent?>,
    private val toDoRepository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _queriedTasks: MutableStateFlow<Resource<List<ToDoTask>>> =
        MutableStateFlow(Resource.Idle)
    val queriedTasks: StateFlow<Resource<List<ToDoTask>>> = _queriedTasks

    private val _searchQuery: MutableStateFlow<String?> = MutableStateFlow(null)
    val searchQuery: StateFlow<String?> = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class)
    val lowPriorityTasks = toDoRepository.sortByLowPriority.flatMapConcat {
        flow {
            emit(Resource.Loading)
            //TODO remove
//            delay(2000L)
            emit(Resource.Success(it))
        }
    }.catch { emit(Resource.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Resource.Idle)

    @OptIn(ExperimentalCoroutinesApi::class)
    val highPriorityTasks = toDoRepository.sortByHighPriority.flatMapConcat {
        flow {
            emit(Resource.Loading)
            //TODO remove
//            delay(2000)
            emit(Resource.Success(it))
        }
    }.catch { emit(Resource.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Resource.Idle)

    val sortState = dataStoreRepository.sortState.map { Resource.Success(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            Resource.Idle
        )

    init {
        collectQueryResult()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun collectQueryResult() {
        _searchQuery.flatMapLatest { query ->
            val queryFlow = if (query == null) {
                toDoRepository.getAllTasks()
            } else {
                toDoRepository.searchTasks(query)
            }.map { Resource.Success(it) }
            flowOf(flowOf(Resource.Loading), queryFlow).flatMapConcat { it }
        }.catch {
            _queriedTasks.value = Resource.Error(it)
        }.onEach {
            _queriedTasks.value = it
        }.launchIn(viewModelScope)
    }

    fun queryTasks(query: String? = null) {
        _searchQuery.value = query
    }

    suspend fun getTask(taskId: Int): ToDoTask? = taskId.takeIf { it > 0 }?.let { id ->
        toDoRepository.getTask(id).firstOrNull()
    }

    fun addTask(toDoTask: ToDoTask) {
        viewModelScope.launch(dispatchers.io) {
            toDoRepository.addTask(toDoTask)
        }
    }

    fun updateTask(toDoTask: ToDoTask) {
        viewModelScope.launch(dispatchers.io) {
            toDoRepository.updateTask(toDoTask)
        }
    }

    fun deleteTask(toDoTask: ToDoTask) {
        viewModelScope.launch(dispatchers.io) {
            toDoRepository.deleteTask(toDoTask)
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch(dispatchers.io) {
            toDoRepository.deleteAllTasks()
        }
    }

    fun sendActionEvent(action: Action, toDoTask: ToDoTask?) {
        actionEventChannel.trySend(ActionEvent(action, toDoTask))
    }

    fun persistSortState(priority: Priority) {
        viewModelScope.launch(dispatchers.io) {
            dataStoreRepository.persistSortState(priority)
        }
    }
}