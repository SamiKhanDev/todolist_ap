package com.example.todolistapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("SELECT * FROM todo_table where isCompleted = 0 ORDER BY id ASC ")
    fun getAllTodos(): LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table WHERE isCompleted = 1 ORDER BY id ASC")
    fun getCompletedTodos(): LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table WHERE id = :id ORDER BY id ASC")
    fun getTodoById(id: Long): Todo?
    @Query("UPDATE todo_table SET isCompleted = 0 WHERE id = :id")
    suspend fun markAsIncomplete(id: Long)
}