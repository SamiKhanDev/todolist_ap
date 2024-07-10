package com.example.todolistapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AddTodoViewModel(private val repository: TodoRepository) : ViewModel() {

    fun addTodo(todo: Todo) {
        viewModelScope.launch {
            repository.insert(todo)
        }
    }
}
