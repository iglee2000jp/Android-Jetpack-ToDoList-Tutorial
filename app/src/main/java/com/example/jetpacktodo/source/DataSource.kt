package com.example.jetpacktodo.source

import androidx.lifecycle.LiveData
import com.example.jetpacktodo.source.model.Task


interface DataSource {
    fun getTasks(): LiveData<Result<List<Task>>>
    fun getTask(taskId: String): LiveData<Result<Task?>>
    suspend fun getTaskById(taskId: String): Result<Task>
    suspend fun insertTask(task: Task)

    suspend fun deleteAllTasks()

    suspend fun deleteTaskById(taskId: String)

    suspend fun updateTask(task: Task)

    suspend fun getExampleTasks(): Result<List<Task>>

    suspend fun taskCompleted(task: Task)

    suspend fun taskActivated(task: Task)

}