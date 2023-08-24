package com.androidisland.todocompose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.data.repository.ToDoRepository
import com.androidisland.todocompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: ToDoRepository) : ViewModel() {

    private val _allTasks = MutableStateFlow<Resource<List<ToDoTask>>>(Resource.Idle)
    val allTasks: StateFlow<Resource<List<ToDoTask>>> = _allTasks
    fun getAllTasks() {
        _allTasks.value = Resource.Loading
        repository.getAllTasks.catch {
            _allTasks.value = Resource.Error(it)
        }.onEach {
            _allTasks.value = Resource.Success(it)
        }.launchIn(viewModelScope)
    }
}