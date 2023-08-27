package com.androidisland.todocompose.ui.screen.task

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.androidisland.todocompose.R
import com.androidisland.todocompose.data.models.Priority
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.util.Action


@Composable
fun TaskAppBar(
    toDoTask: ToDoTask?, navigateToListScreen: (Action) -> Unit
) {
    if (toDoTask == null) {
        NewTaskAppBar(navigateToListScreen = navigateToListScreen)
    } else {
        ExistingTaskAppBar(selectedTask = toDoTask, navigateToListScreen = navigateToListScreen)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskAppBar(
    navigateToListScreen: (Action) -> Unit
) {
    TopAppBar(title = {
        Text(
            text = stringResource(R.string.add_task),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }, navigationIcon = {
        BackAction(onBackClicked = navigateToListScreen)
    }, colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary
    ), actions = {
        AddAction(onAddClicked = navigateToListScreen)
    })
}

@Composable
fun BackAction(
    onBackClicked: (Action) -> Unit
) {
    IconButton(onClick = { onBackClicked(Action.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_arrow),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun AddAction(
    onAddClicked: (Action) -> Unit
) {
    IconButton(onClick = { onAddClicked(Action.ADD) }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.add_task),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExistingTaskAppBar(
    selectedTask: ToDoTask, navigateToListScreen: (Action) -> Unit
) {
    TopAppBar(title = {
        Text(
            text = selectedTask.title,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }, navigationIcon = {
        CloseAction(onCloseClicked = navigateToListScreen)
    }, colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary
    ), actions = {
        DeleteAction(onDeleteClicked = navigateToListScreen)
        UpdateAction(onUpdateClicked = navigateToListScreen)
    })
}

@Composable
fun CloseAction(
    onCloseClicked: (Action) -> Unit
) {
    IconButton(onClick = { onCloseClicked(Action.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.close_icon),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun DeleteAction(
    onDeleteClicked: (Action) -> Unit
) {
    IconButton(onClick = { onDeleteClicked(Action.DELETE) }) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.delete_icon),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun UpdateAction(
    onUpdateClicked: (Action) -> Unit
) {
    IconButton(onClick = { onUpdateClicked(Action.UPDATE) }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.update_icon),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
fun NewTaskAppBarPreview() {
    NewTaskAppBar(navigateToListScreen = {})
}

@Preview
@Composable
fun ExistingTaskAppBarPreview() {
    ExistingTaskAppBar(selectedTask = ToDoTask(1, "Title", "Description goes here", Priority.LOW),
        navigateToListScreen = {})
}