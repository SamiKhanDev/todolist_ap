package com.example.todolistapp.listscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.database.Todo
import com.example.todolistapp.repository.TodoRepository

import kotlinx.coroutines.launch

class TodoListViewModel(private val repository: TodoRepository) : ViewModel() {

    private val _todos = repository.getAllTodos()
    val todos: LiveData<List<Todo>> get() = _todos

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }
    fun completeTodo(todo: Todo) {
        viewModelScope.launch {
            todo.isCompleted = true
            repository.complete(todo)
        }
    }

}
