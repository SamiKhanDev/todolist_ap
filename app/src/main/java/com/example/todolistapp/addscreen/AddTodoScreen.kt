package com.example.todolistapp.addscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.todolistapp.R
import com.example.todolistapp.database.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoScreen(navController: NavHostController, viewModel: AddTodoViewModel) {
    var title by remember { mutableStateOf(TextFieldValue()) }
    var description by remember { mutableStateOf(TextFieldValue()) }
    var titleError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Add Todo") },
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
                value = title,
                onValueChange = {
                    title = it
                    titleError = it.text.isEmpty()
                },
                isError = titleError,
                label = { Text("Title") },
                leadingIcon = { Icon(painterResource(id = R.drawable.ic_title), contentDescription = "Title Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            TextField(
                value = description,
                onValueChange = {
                    description = it
                    descriptionError = it.text.isEmpty()
                },
                isError = descriptionError,
                label = { Text("Description") },
                leadingIcon = { Icon(painterResource(id = R.drawable.ic_description), contentDescription = "description Icon") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                if (title.text.isNotEmpty() && description.text.isNotEmpty()) {
                    viewModel.addTodo(
                        Todo(
                            title = title.text,
                            description = description.text
                        )
                    )
                    navController.popBackStack()
                } else {
                    if (title.text.isEmpty()) {
                        titleError = true
                    }
                    if (description.text.isEmpty()) {
                        descriptionError = true
                    }
                }
            }) {
                Text("Add Todo")
            }
            if (titleError || descriptionError) {
                Text(
                    text = "Please fill out all fields",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}