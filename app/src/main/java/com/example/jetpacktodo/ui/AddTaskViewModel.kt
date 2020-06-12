package com.example.jetpacktodo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpacktodo.Event
import com.example.jetpacktodo.R
import com.example.jetpacktodo.app.ToDoApp
import com.example.jetpacktodo.source.model.Task
import kotlinx.coroutines.launch

class AddTaskViewModel : ViewModel() {
    private val repository = ToDoApp.taskRepository
    val taskTitle = MutableLiveData<String>()
    val taskDescription = MutableLiveData<String>()

    private val _snackBarEvent = MutableLiveData<Event<Int>>()
    val snackBarEvent: LiveData<Event<Int>> = _snackBarEvent
    private val _taskSavedEvent = MutableLiveData<Event<Unit>>()
    val taskSavedEvent: LiveData<Event<Unit>> = _taskSavedEvent

    fun saveTask() {
        val title = taskTitle.value
        val description = taskDescription.value
        if (title.isNullOrBlank() || description.isNullOrBlank()) {
            _snackBarEvent.value = Event(R.string.empty_task_snackbar_message)
        } else {
            val task = Task(taskTitle = title, taskDescription = description)
            saveNewTask(task)
        }
    }

    private fun saveNewTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
            _taskSavedEvent.value = Event(Unit)
        }
    }
}