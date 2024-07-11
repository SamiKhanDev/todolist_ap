package com.example.todolistapp

import TodoListScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todolistapp.addscreen.AddTodoScreen
import com.example.todolistapp.addscreen.AddTodoViewModel
import com.example.todolistapp.database.TodoDatabase
import com.example.todolistapp.listscreen.TodoListViewModel
import com.example.todolistapp.repository.TodoRepository
import com.example.todolistapp.ui.theme.TodolistappTheme
import com.example.todolistapp.updatescreen.UpdateTodoScreen
import com.example.todolistapp.updatescreen.UpdateTodoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodolistappTheme {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController) }
            }
            }
        }

@Composable
fun SetupNavGraph(navController: NavHostController) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = "todo_list",
        enterTransition = { slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(700)
        )
        },
        exitTransition = {slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(700)
        )
        },
        popEnterTransition = { slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(700)
        )
        },
        popExitTransition = {slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(700)
        )
        }
    ) {
        val dataBase =TodoDatabase.getDatabase(context)
        composable(
            route = "todo_list",
        ) {
            val todoListViewModel: TodoListViewModel = viewModel(factory = TodoListViewModelFactory(
                TodoRepository(dataBase.todoDao())
            ))
            TodoListScreen(navController, todoListViewModel)
        }

        composable(
            route = "add_todo",
        ) {
            val addTodoViewModel: AddTodoViewModel = viewModel(factory = AddTodoViewModelFactory(
                TodoRepository(dataBase.todoDao())
            ))
            AddTodoScreen(navController, addTodoViewModel)
        }

        composable(
            route = "update_todo/{todoId}",
            arguments = listOf(navArgument("todoId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val updateTodoViewModel: UpdateTodoViewModel =viewModel(factory = UpdateTodoViewModelFactory(
                TodoRepository(dataBase.todoDao())
            ))
            UpdateTodoScreen(
                navController = navController,
                viewModel = updateTodoViewModel,
                todoId = backStackEntry.arguments?.getLong("todoId") ?: 0L
            )
        }
    }
}