package com.androidisland.todocompose.data.repository

import com.androidisland.todocompose.data.ToDoDao
import com.androidisland.todocompose.data.models.ToDoTask
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@ViewModelScoped
class ToDoRepository @Inject constructor(private val toDoDao: ToDoDao) {

    fun getAllTasks(): Flow<List<ToDoTask>> = toDoDao.getAllTasks()

    val sortByLowPriority: Flow<List<ToDoTask>> by lazy {
        toDoDao.sortByLowPriority()
    }

    val sortByHighPriority: Flow<List<ToDoTask>> by lazy {
        toDoDao.sortByHighPriority()
    }

    fun getTask(taskId: Int): Flow<ToDoTask> = toDoDao.getTask(taskId)
    suspend fun addTask(toDoTask: ToDoTask) = toDoDao.addTask(toDoTask)

    suspend fun updateTask(toDoTask: ToDoTask) = toDoDao.updateTask(toDoTask)
    suspend fun deleteTask(toDoTask: ToDoTask) = toDoDao.deleteTask(toDoTask)

    suspend fun deleteAllTasks() = toDoDao.deleteAllTasks()

    fun searchTasks(searchQuery: String): Flow<List<ToDoTask>> =
        toDoDao.searchTasks("%$searchQuery%")
}