package com.example.jetpacktodo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpacktodo.Event
import com.example.jetpacktodo.R
import com.example.jetpacktodo.app.ToDoApp
import com.example.jetpacktodo.source.Result.Success
import com.example.jetpacktodo.source.model.Task
import kotlinx.coroutines.launch

class EditTaskViewModel : ViewModel() {
    private val repository = ToDoApp.taskRepository
    val taskTitle = MutableLiveData<String>()
    val taskDescription = MutableLiveData<String>()
    private val _taskId = MutableLiveData<String>()
    private val _snackBarEvent = MutableLiveData<Event<Int>>()
    val snackBarEvent: LiveData<Event<Int>> = _snackBarEvent
    private val _taskUpdatedEvent = MutableLiveData<Event<Unit>>()
    val taskUpdatedEvent: LiveData<Event<Unit>> = _taskUpdatedEvent

    fun loadTask(taskId: String) {
        if (taskId == _taskId.value) {
            return
        }
        _taskId.value = taskId
        viewModelScope.launch {
            repository.getTaskById(taskId).let { result ->
                if (result is Success) {
                    onTaskLoad(result.data)
                } else {
                    //message
                    return@launch
                }
            }
        }
    }

    private fun onTaskLoad(task: Task) {
        taskTitle.value = task.taskTitle
        taskDescription.value = task.taskDescription
    }

    fun updateTask() {
        val title = taskTitle.value
        val description = taskDescription.value
        if (title.isNullOrBlank() || description.isNullOrBlank()) {
            _snackBarEvent.value = Event(R.string.empty_task_snackbar_message)
        } else {
            val task =
                Task(taskId = _taskId.value!!, taskTitle = title, taskDescription = description)
            updateCompleted(task)
        }
    }

    private fun updateCompleted(task: Task) = viewModelScope.launch {
        repository.updateTask(task)
        _taskUpdatedEvent.value = Event(Unit)
    }
}