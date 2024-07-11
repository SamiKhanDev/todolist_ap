package com.example.todolistapp.addscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.database.Todo
import com.example.todolistapp.repository.TodoRepository
import kotlinx.coroutines.launch

class AddTodoViewModel(private val repository: TodoRepository) : ViewModel() {

    fun addTodo(todo: Todo) {
        viewModelScope.launch {
            repository.insert(todo)
        }
    }
}
