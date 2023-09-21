package com.androidisland.todocompose

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.androidisland.todocompose.common.SharedViewModel
import com.androidisland.todocompose.common.theme.ToDoAppTheme
import com.androidisland.todocompose.navigation.Actions
import com.androidisland.todocompose.navigation.SetUpNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            ToDoApp(
                intent = intent,
                navController = navController
            )
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ToDoApp(
    intent: Intent,
    navController: NavHostController,
    sharedViewModel: SharedViewModel = hiltViewModel()
) {

    ToDoAppTheme {
        val actions = remember {
            Actions(navController)
        }
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) {
            SetUpNavGraph(
                intent,
                navController,
                actions,
                sharedViewModel
            )
        }
    }
}