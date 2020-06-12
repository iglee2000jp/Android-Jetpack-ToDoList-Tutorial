package com.example.jetpacktodo.ui

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.example.jetpacktodo.*
import com.example.jetpacktodo.app.ToDoApp
import com.example.jetpacktodo.source.Result
import com.example.jetpacktodo.source.Result.Success
import com.example.jetpacktodo.source.model.Task
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {
    private val repository = ToDoApp.taskRepository
    private val _currentFilterType = MutableLiveData(FilterType.SHOW_ALL)
    private val _taskItems = repository.getTasks().switchMap {
        checkResult(it)
    }
    private val filteredTasks = _currentFilterType.switchMap {
        _taskItems.map { filteringTasks(it) }
    }

    private fun filteringTasks(list: List<Task>): List<Task> {
        var filteredTasks = mutableListOf<Task>()
        //filtering takes time
        viewModelScope.launch {
            filteredTasks = filtering(list, _currentFilterType.value!!)
        }
        return filteredTasks
    }

    private fun filtering(tasks: List<Task>, filterType: FilterType): MutableList<Task> {
        val filteredTasks = mutableListOf<Task>()
        for (task in tasks) {
            when (filterType) {
                FilterType.SHOW_ALL -> filteredTasks.add(task)
                FilterType.SHOW_COMPLETED -> if (task.isCompleted) {
                    filteredTasks.add(task)
                }
                FilterType.SHOW_ACTIVE -> if (!task.isCompleted) {
                    filteredTasks.add(task)
                }
            }
        }
        return filteredTasks
    }

    val tasks = filteredTasks
    val empty = Transformations.map(filteredTasks) {
        it.isEmpty()
    }
    private val _navigateToDetails = MutableLiveData<Event<String>>()
    val navigateToDetails: LiveData<Event<String>>
        get() = _navigateToDetails
    private val _navigateToAddTask = MutableLiveData<Event<Unit>>()
    val navigateToAddTask: LiveData<Event<Unit>>
        get() = _navigateToAddTask

    private val _snackBarEvent = MutableLiveData<Event<Int>>()
    val snackBarEvent: LiveData<Event<Int>> = _snackBarEvent
    private var messageShown = false

    @SuppressLint("NullSafeMutableLiveData")
    private fun checkResult(result: Result<List<Task>>): LiveData<List<Task>> {
        val tasks = MutableLiveData<List<Task>>()
        if (result is Success) {
            tasks.value = result.data
        } else {
            tasks.value = emptyList()
        }
        return tasks
    }

    fun navigateToDetails(taskId: String) {
        _navigateToDetails.value = Event(taskId)
    }

    fun navigateToAddTask() {
        _navigateToAddTask.value = Event(Unit)
    }

    fun showResult(result: Int) {
        if (messageShown) return
        when (result) {
            ADD_TASK_OK -> setSnackBarEvent(R.string.saved_task_successfully)
            DELETE_TASK_OK -> setSnackBarEvent(R.string.task_deleted_successfully)
            EDIT_TASK_OK -> setSnackBarEvent(R.string.task_edit_successfully)
        }
        messageShown = true
    }

    private fun setSnackBarEvent(message: Int) {
        _snackBarEvent.value = Event(message)
    }

    fun updateCheckbox(task: Task, isChecked: Boolean) = viewModelScope.launch {
        if (isChecked) {
            repository.taskCompleted(task)
            _snackBarEvent.value = Event(R.string.task_marked_completed)
        } else {
            repository.taskActivated(task)
            _snackBarEvent.value = Event(R.string.task_marked_active)
        }
    }

    fun selectFilterType(filterType: FilterType) {
        _currentFilterType.value = filterType
    }
}