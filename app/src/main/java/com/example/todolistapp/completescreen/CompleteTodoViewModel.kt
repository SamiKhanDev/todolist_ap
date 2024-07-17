package com.example.todolistapp.completescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.database.Todo
import com.example.todolistapp.repository.TodoRepository
import kotlinx.coroutines.launch

class CompleteTodoViewModel(private val repository: TodoRepository) : ViewModel() {
    val completedTodos = repository.getCompletedTodos()

    fun complete(todo: Todo) {
        viewModelScope.launch {
            repository.complete(todo)
        }
    }

    fun deleteCompletedTodo(todo: Todo) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }
    fun markAsIncomplete(todo: Todo) {
        viewModelScope.launch {
            repository.markAsIncomplete(todo)
        }
    }
}

