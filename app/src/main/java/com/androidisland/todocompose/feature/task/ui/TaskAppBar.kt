package com.androidisland.todocompose.feature.task.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.androidisland.todocompose.R
import com.androidisland.todocompose.component.DisplayAlertDialog
import com.androidisland.todocompose.data.models.Priority
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.util.Action


@Composable
fun TaskAppBar(
    toDoTask: ToDoTask?,
    onActionClicked: (Action) -> Unit
) {
    if (toDoTask == null) {
        NewTaskAppBar(onActionClicked = onActionClicked)
    } else {
        ExistingTaskAppBar(selectedTask = toDoTask, onActionClicked = onActionClicked)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskAppBar(
    onActionClicked: (Action) -> Unit
) {
    TopAppBar(title = {
        Text(
            text = stringResource(R.string.add_task),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }, navigationIcon = {
        BackAction(onBackClicked = onActionClicked)
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary
    ), actions = {
        AddAction(onAddClicked = onActionClicked)
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
    selectedTask: ToDoTask,
    onActionClicked: (Action) -> Unit
) {
    TopAppBar(title = {
        Text(
            text = selectedTask.title,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }, navigationIcon = {
        CloseAction(onCloseClicked = onActionClicked)
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary
    ), actions = {
        ExistingTaskAppBarActions(selectedTask, onActionClicked)
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
fun ExistingTaskAppBarActions(
    selectedTask: ToDoTask,
    onActionClicked: (Action) -> Unit
) {
    var isDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val message = buildAnnotatedString {
        val original = stringResource(id = R.string.delete_task_confirm_msg, selectedTask.title)
        append(original)
        val start = original.indexOf(selectedTask.title)
        addStyle(
            style = SpanStyle(fontWeight = FontWeight.Bold),
            start = start,
            end = start + selectedTask.title.length
        )
    }

    DisplayAlertDialog(title = stringResource(id = R.string.delete_task_confirm_title),
        message = message,
        isDialogVisible = isDialogVisible,
        closeDialog = { isDialogVisible = false }) {
        onActionClicked(Action.DELETE)
    }

    DeleteAction(onDeleteClicked = {
        isDialogVisible = true
    })
    UpdateAction(onUpdateClicked = onActionClicked)
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
    NewTaskAppBar(onActionClicked = {})
}

@Preview
@Composable
fun ExistingTaskAppBarPreview() {
    ExistingTaskAppBar(selectedTask = ToDoTask(1, "Title", "Description goes here", Priority.LOW),
        onActionClicked = {})
}