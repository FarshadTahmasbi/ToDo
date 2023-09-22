package com.androidisland.todocompose.data.repository

import com.androidisland.todocompose.data.ToDoDao
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.enums.PrioritySort
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@ViewModelScoped
class ToDoRepository @Inject constructor(private val toDoDao: ToDoDao) {

    fun getTasks(sort: PrioritySort): Flow<List<ToDoTask>> {
        return when (sort) {
            PrioritySort.DEFAULT -> toDoDao.getAllTasks()
            PrioritySort.LOW_TO_HIGH -> toDoDao.sortByLowPriority()
            PrioritySort.HIGH_TO_LOW -> toDoDao.sortByHighPriority()
        }
    }

    fun getTask(taskId: Int): Flow<ToDoTask> = toDoDao.getTask(taskId)
    suspend fun insertTask(toDoTask: ToDoTask) = toDoDao.insertTask(toDoTask)

    suspend fun updateTask(toDoTask: ToDoTask) = toDoDao.updateTask(toDoTask)
    suspend fun deleteTask(toDoTask: ToDoTask) = toDoDao.deleteTask(toDoTask)

    suspend fun deleteTasks() = toDoDao.deleteAllTasks()

    fun queryTasks(searchQuery: String): Flow<List<ToDoTask>> =
        toDoDao.searchTasks("%$searchQuery%")

    fun isValidTask(task: ToDoTask) =
        task.title.isNotEmpty() && task.description.isNotEmpty()
}