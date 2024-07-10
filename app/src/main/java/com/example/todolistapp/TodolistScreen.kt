import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.todolistapp.Todo
import com.example.todolistapp.TodoListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(navController: NavHostController, viewModel: TodoListViewModel) {
    val todoList by viewModel.todos.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Todo List") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add_todo")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Todo")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (todoList.isEmpty()) {
                Text(text = "No items yet", modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                TodoList(todoList, onDelete = { todo ->
                    viewModel.deleteTodo(todo)
                }, onUpdate = { todo ->
                    navController.navigate("update_todo/${todo.id}")
                })
            }
        }
    }
}

@Composable
fun TodoList(
    todos: List<Todo>,
    onDelete: (Todo) -> Unit,
    onUpdate: (Todo) -> Unit
) {
    LazyColumn {
        items(todos) { todo ->
            TodoItem(todo = todo, onDelete = onDelete, onUpdate = onUpdate)
        }
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    onDelete: (Todo) -> Unit,
    onUpdate: (Todo) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = todo.title,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = todo.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onDelete(todo) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
            IconButton(onClick = { onUpdate(todo) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    }
}