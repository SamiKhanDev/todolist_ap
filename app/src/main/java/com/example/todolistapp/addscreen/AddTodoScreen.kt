package com.example.todolistapp.addscreen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.todolistapp.R
import com.example.todolistapp.database.Todo
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoScreen(navController: NavHostController, viewModel: AddTodoViewModel) {
    val context = LocalContext.current
    var title by remember { mutableStateOf(TextFieldValue()) }
    var description by remember { mutableStateOf(TextFieldValue()) }
    var titleError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val dateTime = remember { mutableStateOf(TextFieldValue("")) }
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
                leadingIcon = { Icon(painterResource(id = R.drawable.ic_description), contentDescription = "Description Icon") },
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
                if (title.text.isNotEmpty() && description.text.isNotEmpty()) {
                    viewModel.addTodo(
                        Todo(
                            title = title.text,
                            description = description.text,
                            date = dateTime.value.text.split(" ")[0],
                            time = dateTime.value.text.split(" ")[1]
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