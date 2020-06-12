package com.example.jetpacktodo.app

import android.content.Context
import androidx.room.Room
import com.example.jetpacktodo.source.DataSource
import com.example.jetpacktodo.source.TaskRepository
import com.example.jetpacktodo.source.local.FakeLocalDataSource
import com.example.jetpacktodo.source.local.LocalDataSource
import com.example.jetpacktodo.source.local.TaskDatabase

object ServiceLocator {

    fun provideFakeTaskRepository(): TaskRepository {
        return TaskRepository(FakeLocalDataSource())
    }

    private var database: TaskDatabase? = null

    @Volatile
    var taskRepository: TaskRepository? = null

    fun provideTaskRepository(context: Context): TaskRepository {
        synchronized(this) {
            return taskRepository ?: createTaskRepository(context)
        }
    }

    private fun createTaskRepository(context: Context): TaskRepository {
        val newRepository = TaskRepository(createLocalDataSource(context))
        taskRepository = newRepository
        return newRepository
    }

    private fun createLocalDataSource(context: Context): DataSource {
        val database = database ?: createDatabase(context)
        return LocalDataSource(database.taskDao())
    }

    private fun createDatabase(context: Context): TaskDatabase {
        val db = Room.databaseBuilder(
            context.applicationContext,
            TaskDatabase::class.java,
            TaskDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
        database = db
        return db
    }
}