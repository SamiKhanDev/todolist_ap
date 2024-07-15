package com.example.todolistapp.updatescreen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.util.Calendar


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
    val context = LocalContext.current
    val dateTime = remember { mutableStateOf(TextFieldValue("")) }
    val todo = viewModel.selectedTodo.observeAsState()
    val calendar = Calendar.getInstance()

    val timePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, hour: Int, minute: Int ->
            val time = "$hour:$minute"
            dateTime.value = TextFieldValue(dateTime.value.text + " $time")
        },
        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
    )

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val date = "$dayOfMonth/${month + 1}/$year"
            dateTime.value = TextFieldValue(date)
            timePickerDialog.show()
        },
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )

    todo.value?.let { todo ->
        Log.d("TestABC", " Updating screen for ${todo}")
        var title by remember { mutableStateOf(todo.title) }
        var description by remember { mutableStateOf(todo.description) }
        LaunchedEffect(key1 = todo) {
            title = todo.title
            description = todo.description



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
                    value = title,
                    onValueChange = { title =it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                TextField(
                    value = description,
                    onValueChange = { description=it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                TextField(
                    value = dateTime.value,
                    onValueChange = { },
                    label = { Text("Date and Time") },
                    readOnly = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clickable { datePickerDialog.show() }
                )

                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    todo.copy(
                        title = title,
                        description = description
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
