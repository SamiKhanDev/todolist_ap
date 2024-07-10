package com.example.todolistapp

import androidx.lifecycle.LiveData

class TodoRepository(private val todoDao: TodoDao) {

    fun getAllTodos(): LiveData<List<Todo>> {
        return todoDao.getAllTodos()
    }

    suspend fun getTodoById(id: Long): Todo? {
        return todoDao.getTodoById(id)
    }

    suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    suspend fun update(todo: Todo) {
        todoDao.update(todo)
    }

    suspend fun delete(todo: Todo) {
        todoDao.delete(todo)
    }
}
