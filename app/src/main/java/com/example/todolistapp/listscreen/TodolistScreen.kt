import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.todolistapp.database.Todo
import com.example.todolistapp.listscreen.TodoListViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(navController: NavHostController, viewModel: TodoListViewModel) {
    val todoList by viewModel.todos.observeAsState(emptyList())
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Todo List") },
                actions = {
                    IconButton(onClick = { navController.navigate("completed_todo") }) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Completed Tasks")
                    }
                }

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
                LazyColumn {
                    todoList.groupBy { it.date }.forEach { (date, todos) ->
                        item {
                            Text(
                                text = date,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(todos) { todo ->
                            val backgroundColor = getBackgroundColor()
                            ToDoSwipeableItem(
                                todo = todo,
                                onDelete = { viewModel.deleteTodo(it) },
                                onUpdate = { navController.navigate("update_todo/${it.id}") },
                                onComplete = { completedTodo ->
                                    viewModel.completeTodo(completedTodo)
                                    Toast.makeText(context, "Task completed", Toast.LENGTH_SHORT)
                                        .show()
                                },
                                backgroundColor = backgroundColor
                            )
                        }
                    }
                }
            }
        }
    }
}

fun formatTimeToAmPm(time: String): String {
    val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val date = inputFormat.parse(time)
    return outputFormat.format(date!!)
}

fun getBackgroundColor(): Color {
    val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan, Color.Magenta)
    return colors[Random().nextInt().coerceIn(0, 5) % colors.size]
}

@Composable
fun TodoItem(
    todo: Todo,
    onComplete: (Todo) -> Unit,
    backgroundColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor.copy(alpha = 0.05f))
            .padding(start = 16.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onComplete(todo) }) {
            Icon(Icons.Default.CheckCircle, contentDescription = "Complete")
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = formatTimeToAmPm(todo.time),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = todo.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = todo.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoSwipeableItem(
    todo: Todo,
    onDelete: (Todo) -> Unit,
    onUpdate: (Todo) -> Unit,
    onComplete: (Todo) -> Unit,
    backgroundColor: Color
) {

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Delete Confirmation") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                Button(onClick = {

                    onDelete(todo)
                    showDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    val context = LocalContext.current
    val currentItem by rememberUpdatedState(todo)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                   showDialog = true
                    return@rememberSwipeToDismissBoxState false
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    onUpdate(currentItem)
                    return@rememberSwipeToDismissBoxState false
                }

                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
            }
        },
        positionalThreshold = { it * 1f }
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = Modifier,
        backgroundContent = { DismissBackground(dismissState) },
        content = {
            TodoItem(
                todo, onComplete, backgroundColor
            )
        })

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Color(0xFFFF1744)
        SwipeToDismissBoxValue.EndToStart -> Color(0xFF1DE9B6)
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
    }
}