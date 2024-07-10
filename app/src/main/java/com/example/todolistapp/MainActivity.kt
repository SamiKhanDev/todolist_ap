package com.example.todolistapp

import TodoListScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todolistapp.ui.theme.TodolistappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val todoDao = TodoDatabase.getDatabase(this).todoDao()
        val repository = TodoRepository(todoDao)

        enableEdgeToEdge()
        setContent {
            TodolistappTheme {

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "todo_list") {
                    composable("todo_list") {
                        val viewModel: TodoListViewModel = viewModel(
                            factory = TodoListViewModelFactory(repository)
                        )
                        TodoListScreen(navController = navController, viewModel = viewModel)
                    }
                    composable("add_todo") {
                        val viewModel: AddTodoViewModel = viewModel(
                            factory = AddTodoViewModelFactory(repository)
                        )
                        AddTodoScreen(navController = navController, viewModel = viewModel)
                    }
                    composable(
                        "update_todo/{todoId}",
                        arguments = listOf(navArgument("todoId") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val viewModel: UpdateTodoViewModel = viewModel(
                            factory = UpdateTodoViewModelFactory(repository)
                        )
                        val todoId = backStackEntry.arguments?.getLong("todoId") ?: 0L
                        UpdateTodoScreen(navController = navController, viewModel = viewModel, todoId = todoId)
                    }
                }
            }
            }
        }
    }



