package com.androidisland.todocompose.ui.screen.list

import android.util.Log
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.androidisland.todocompose.R
import com.androidisland.todocompose.ui.theme.ToDoComposeTheme
import com.androidisland.todocompose.ui.viewmodel.SharedViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    sharedViewModel: SharedViewModel,
    navigateToTaskScreen: (Int) -> Unit
) {
    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllTasks()
    }
    val allTasksResource by sharedViewModel.allTasks.collectAsState()

    Scaffold(topBar = {
        ListAppBar(
            onSortClicked = {
                //TODO sort
                Log.d("test123", "Sort: $it")
            },
            onDeleteClicked = {
                //TODO delete
                Log.d("test123", "Delete")
            },
            onSearchClicked = {
                //TODO search for it
                Log.d("test123", "Searching for $it")
            })
    }, content = {
        ListContent(
            tasksResource = allTasksResource,
            navigateToTaskScreen = navigateToTaskScreen,
            contentPadding = it
        )
    }, floatingActionButton = { ListFab(navigateToTaskScreen) })
}

@Composable
fun ListFab(
    onFabClicked: (Int) -> Unit
) {
    FloatingActionButton(
        onClick = { onFabClicked(-1) }, shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Filled.Add, contentDescription = stringResource(
                id = R.string.add_button
            ), tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
private fun ListScreenPreview() {
    ToDoComposeTheme {
        ListScreen(hiltViewModel(), navigateToTaskScreen = {})
    }
}
