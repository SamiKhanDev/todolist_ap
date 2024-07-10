package com.example.todolistapp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UpdateTodoViewModel(private val repository: TodoRepository) : ViewModel() {

    private val _selectedTodo = MutableLiveData<Todo?>()
    val selectedTodo: LiveData<Todo?> get() = _selectedTodo

    fun getUpdatedTodo(id: Long) {
        viewModelScope.launch {
            _selectedTodo.value = repository.getTodoById(id)
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            repository.update(todo)
        }
    }
}
