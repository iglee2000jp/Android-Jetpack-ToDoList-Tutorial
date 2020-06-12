package com.example.jetpacktodo.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.jetpacktodo.source.DataSource
import com.example.jetpacktodo.source.Result
import com.example.jetpacktodo.source.Result.Error
import com.example.jetpacktodo.source.Result.Success
import com.example.jetpacktodo.source.model.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LocalDataSource(
    private val taskDao: TaskDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataSource {
    override fun getTasks(): LiveData<Result<List<Task>>> {
        return taskDao.getTasks().map { Success(it) }
    }

    override fun getTask(taskId: String): LiveData<Result<Task?>> {
        return taskDao.getTask(taskId).map { Success(it) }
    }

    override suspend fun getTaskById(taskId: String): Result<Task> = withContext(ioDispatcher) {
        try {
            val task = taskDao.getTaskById(taskId)
            if (task != null) {
                return@withContext Success(task)
            } else {
                return@withContext Error(Exception("Task not found"))
            }
        } catch (e: Exception) {
            return@withContext Error(e)
        }
    }

    override suspend fun insertTask(task: Task) {
        withContext(ioDispatcher) {
            taskDao.insertTask(task)
        }
    }

    override suspend fun deleteAllTasks() {
        withContext(ioDispatcher) {
            taskDao.deleteAllTasks()
        }
    }

    override suspend fun deleteTaskById(taskId: String) {
        withContext(ioDispatcher) {
            taskDao.deleteTaskById(taskId)
        }
    }

    override suspend fun updateTask(task: Task) {
        withContext(ioDispatcher) {
            taskDao.updateTask(task)
        }
    }

    override suspend fun getExampleTasks(): Result<List<Task>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Success(taskDao.getExampleTasks())
            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun taskCompleted(task: Task) {
        withContext(ioDispatcher) {
            taskDao.updateTaskCompleted(task.taskId, isCheck = true)
        }
    }

    override suspend fun taskActivated(task: Task) {
        withContext(ioDispatcher) {
            taskDao.updateTaskCompleted(task.taskId, isCheck = false)
        }
    }
}