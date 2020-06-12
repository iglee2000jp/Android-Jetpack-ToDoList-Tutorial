package com.example.jetpacktodo.app

import android.app.Application
import com.example.jetpacktodo.BuildConfig
import com.example.jetpacktodo.source.TaskRepository
import timber.log.Timber

class ToDoApp : Application() {

    companion object {
        private lateinit var instance: ToDoApp
        val taskRepository: TaskRepository
            get() = ServiceLocator.provideTaskRepository(instance)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}