package com.androidisland.todocompose

import android.content.Intent
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.androidisland.todocompose.navigation.Actions
import com.androidisland.todocompose.navigation.SetUpToDoAppNavigation
import com.androidisland.todocompose.ui.theme.ToDoAppTheme
import com.androidisland.todocompose.ui.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            ToDoApp(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }
}

@Composable
fun ToDoApp(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
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