package com.example.todolistapp

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTodoScreen(
    navController: NavHostController,
    viewModel: UpdateTodoViewModel,
    todoId: Long
) {
    LaunchedEffect(todoId) {
        Log.d("TestABC", todoId.toString())
        viewModel.getUpdatedTodo(todoId)
    }
    val todo = viewModel.selectedTodo.observeAsState()

    todo.value?.let { todo ->
        Log.d("TestABC", " Updating screen for ${todo}")
        val title = remember { mutableStateOf(todo.title) }
        val description = remember { mutableStateOf(todo.description) }
        LaunchedEffect(key1 = todo) {
            title.value = todo.title
            description.value = todo.description
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Update Todo") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                TextField(
                    value = title.value.toString(),
                    onValueChange = { title.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                TextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    todo.copy(
                        title = title.toString(),
                        description = description.toString()
                    )
                        ?.let { viewModel.updateTodo(it) }

                    navController.popBackStack()
                }) {
                    Text("Update")
                }
            }
        }
    }

}
