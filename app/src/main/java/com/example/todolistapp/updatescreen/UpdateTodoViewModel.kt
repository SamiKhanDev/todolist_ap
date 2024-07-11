package com.example.todolistapp.updatescreen
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.database.Todo
import com.example.todolistapp.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateTodoViewModel(private val repository: TodoRepository) : ViewModel() {

    private val _selectedTodo = MutableLiveData<Todo?>()
    val selectedTodo: LiveData<Todo?> get() = _selectedTodo

    fun getUpdatedTodo(id: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){

                _selectedTodo.postValue(repository.getTodoById(id))
            }
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            repository.update(todo)
        }
    }
}
