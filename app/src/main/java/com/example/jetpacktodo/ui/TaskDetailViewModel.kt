package com.example.jetpacktodo.ui

import androidx.lifecycle.*
import com.example.jetpacktodo.Event
import com.example.jetpacktodo.R
import com.example.jetpacktodo.app.ToDoApp
import com.example.jetpacktodo.source.Result
import com.example.jetpacktodo.source.Result.Success
import com.example.jetpacktodo.source.model.Task
import kotlinx.coroutines.launch

class TaskDetailViewModel : ViewModel() {
    private val repository = ToDoApp.taskRepository
    private val _taskId = MutableLiveData<String>()
    private val _task = _taskId.switchMap { taskId ->
        repository.getTask(taskId).map { checkResult(it) }
    }
    val task = _task
    private val _deleteTaskEvent = MutableLiveData<Event<Unit>>()
    val deleteTaskEvent: LiveData<Event<Unit>> = _deleteTaskEvent
    private val _snackBarEvent = MutableLiveData<Event<Int>>()
    val snackBarEvent: LiveData<Event<Int>> = _snackBarEvent
    private val _navigateToEdit = MutableLiveData<Event<String>>()
    val navigateToEdit: LiveData<Event<String>> = _navigateToEdit

    private fun checkResult(result: Result<Task?>): Task? {
        return if (result is Success) {
            result.data
        } else {
            //message
            null
        }
    }

    fun loadTask(taskId: String) {
        if (taskId == _taskId.value) {
            return
        }
        _taskId.value = taskId
    }

    fun deleteTask() {
        viewModelScope.launch {
            _taskId.value?.let {
                repository.deleteTaskById(it)
                _deleteTaskEvent.value = Event(Unit)
            }
        }
    }

    fun updateCheckbox(isChecked: Boolean) = viewModelScope.launch {
        val task = _task.value ?: return@launch
        if (isChecked) {
            repository.taskCompleted(task)
            _snackBarEvent.value = Event(R.string.task_marked_completed)
        } else {
            repository.taskActivated(task)
            _snackBarEvent.value = Event(R.string.task_marked_active)
        }
    }

    fun navigateToEdit() {
        val taskId = _taskId.value ?: return
        _navigateToEdit.value = Event(taskId)
    }
}