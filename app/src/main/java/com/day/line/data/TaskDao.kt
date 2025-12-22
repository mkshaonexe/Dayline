package com.day.line.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    
    @Query("SELECT * FROM tasks WHERE date = :date ORDER BY startTime ASC")
    fun getTasksForDate(date: String): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks ORDER BY date DESC, startTime ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT COUNT(*) FROM tasks WHERE date = :date")
    fun getTaskCountForDate(date: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE date = :date AND isCompleted = 1")
    fun getCompletedTaskCountForDate(date: String): Flow<Int>

    @Query("SELECT DISTINCT date FROM tasks WHERE isCompleted = 1 ORDER BY date DESC")
    fun getDatesWithCompletedTasks(): Flow<List<String>>
    
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Long): Task?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long
    
    @Update
    suspend fun updateTask(task: Task)
    
    @Delete
    suspend fun deleteTask(task: Task)
    
    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Long)
    
    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()
}
