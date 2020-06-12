package com.example.jetpacktodo.source

import androidx.lifecycle.LiveData
import com.example.jetpacktodo.source.model.Task

class TaskRepository(private val localDataSource: DataSource) {

    fun getTasks(): LiveData<Result<List<Task>>> {
        return localDataSource.getTasks()
    }

    fun getTask(taskId: String): LiveData<Result<Task?>> {
        return localDataSource.getTask(taskId)
    }

    suspend fun insertTask(task: Task) {
        localDataSource.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        localDataSource.updateTask(task)
    }

    suspend fun deleteTaskById(taskId: String) {
        localDataSource.deleteTaskById(taskId)
    }

    suspend fun deleteAllTasks() {
        localDataSource.deleteAllTasks()
    }

    suspend fun taskCompleted(task: Task) {
        localDataSource.taskCompleted(task)
    }

    suspend fun taskActivated(task: Task) {
        localDataSource.taskActivated(task)
    }

    suspend fun getTaskById(taskId: String): Result<Task> {
        return localDataSource.getTaskById(taskId)
    }
}