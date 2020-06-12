package com.example.jetpacktodo.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.jetpacktodo.source.DataSource
import com.example.jetpacktodo.source.Result
import com.example.jetpacktodo.source.Result.Success
import com.example.jetpacktodo.source.model.Task

class FakeLocalDataSource : DataSource {

    private fun getFakeTasks(): LiveData<List<Task>> {
        val result = MutableLiveData<List<Task>>()
        val tasks = mutableListOf<Task>()
        for (i in 0 until 100) {
            val task = Task()
            task.taskTitle = "TEST TASK #$i"
            task.isCompleted = i % 3 == 0
            task.taskDescription = "test task ######$i"
            tasks.add(task)
        }
        result.value = tasks
        return result
    }

    override fun getTasks(): LiveData<Result<List<Task>>> {
        return getFakeTasks().map { Success(it) }
    }

    override fun getTask(taskId: String): LiveData<Result<Task?>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskById(taskId: String): Result<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun insertTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskById(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun getExampleTasks(): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun taskCompleted(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun taskActivated(task: Task) {
        TODO("Not yet implemented")
    }
}