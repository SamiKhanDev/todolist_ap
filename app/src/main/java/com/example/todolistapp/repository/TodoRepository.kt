package com.example.todolistapp.repository
import androidx.lifecycle.LiveData
import com.example.todolistapp.database.Todo
import com.example.todolistapp.database.TodoDao

class TodoRepository(private val todoDao: TodoDao) {


    fun getAllTodos(): LiveData<List<Todo>> {
        return todoDao.getAllTodos()
    }


    fun getCompletedTodos(): LiveData<List<Todo>> {
        return todoDao.getCompletedTodos()
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


    suspend fun complete(id: Todo) {
        id.isCompleted = true
        todoDao.update(id)
    }
    suspend fun markAsIncomplete(todo: Todo) {
        todo.isCompleted = false
        todoDao.update(todo)
    }
}
