package com.androidisland.todocompose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.data.repository.ToDoRepository
import com.androidisland.todocompose.ext.toResourceStateFlow
import com.androidisland.todocompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: ToDoRepository) : ViewModel() {

    val allTasks: StateFlow<Resource<List<ToDoTask>>> by lazy {
        repository.getAllTasks.toResourceStateFlow(viewModelScope)
    }

    suspend fun getTask(taskId: Int): ToDoTask? = repository.getTask(taskId).firstOrNull()

}