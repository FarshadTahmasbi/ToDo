package com.androidisland.todocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.androidisland.todocompose.navigation.Actions
import com.androidisland.todocompose.navigation.SetUpToDoAppNavigation
import com.androidisland.todocompose.ui.theme.ToDoAppTheme
import com.androidisland.todocompose.ui.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val sharedViewModel: SharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoApp(sharedViewModel = sharedViewModel)
        }
    }
}

@Composable
fun ToDoApp(sharedViewModel: SharedViewModel) {
    val navController = rememberNavController()
    val actions = remember {
        Actions(navController)
    }
    ToDoAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SetUpToDoAppNavigation(
                navController,
                actions,
                sharedViewModel
            )
        }
    }
}