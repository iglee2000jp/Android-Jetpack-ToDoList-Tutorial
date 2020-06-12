package com.example.jetpacktodo.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.jetpacktodo.source.model.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table")
    fun getTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE id = :taskId")
    fun getTask(taskId: String): LiveData<Task?>

    @Query("SELECT * FROM task_table WHERE id = :taskId")
    suspend fun getTaskById(taskId: String): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("DELETE FROM task_table WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: String)

    @Query("DELETE FROM task_table")
    suspend fun deleteAllTasks()

    @Query("UPDATE task_table SET completed =:isCheck WHERE id = :taskId ")
    suspend fun updateTaskCompleted(taskId: String, isCheck: Boolean)

    //example
    @Query("SELECT * FROM task_table")
    suspend fun getExampleTasks(): List<Task>
}