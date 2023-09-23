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
import com.androidisland.todocompose.enums.TaskAction


@Composable
fun TaskAppBar(
    taskTitle: String?,
    onCloseClicked: () -> Unit,
    onActionClicked: (TaskAction) -> Unit
) {
    if (taskTitle == null) {
        NewTaskAppBar(
            onBackClicked = onCloseClicked, onActionClicked = onActionClicked
        )
    } else {
        ExistingTaskAppBar(
            taskTitle = taskTitle,
            onCloseClicked = onCloseClicked,
            onActionClicked = onActionClicked
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskAppBar(
    onBackClicked: () -> Unit,
    onActionClicked: (TaskAction) -> Unit
) {
    TopAppBar(title = {
        Text(
            text = stringResource(R.string.add_task),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }, navigationIcon = {
        BackAction(onBackClicked = onBackClicked)
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary
    ), actions = {
        AddAction(onAddClicked = onActionClicked)
    })
}

@Composable
fun BackAction(
    onBackClicked: () -> Unit
) {
    IconButton(onClick = onBackClicked) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_arrow),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun AddAction(
    onAddClicked: (TaskAction) -> Unit
) {
    IconButton(onClick = { onAddClicked(TaskAction.INSERT_OR_UPDATE) }) {
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
    taskTitle: String,
    onCloseClicked: () -> Unit,
    onActionClicked: (TaskAction) -> Unit
) {
    TopAppBar(title = {
        Text(
            text = taskTitle,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }, navigationIcon = {
        CloseAction(onCloseClicked = onCloseClicked)
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary
    ), actions = {
        ExistingTaskAppBarActions(taskTitle, onActionClicked)
    })
}

@Composable
fun CloseAction(
    onCloseClicked: () -> Unit
) {
    IconButton(onClick = onCloseClicked) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.close_icon),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun ExistingTaskAppBarActions(
    taskTitle: String,
    onActionClicked: (TaskAction) -> Unit
) {
    var isDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val message = buildAnnotatedString {
        val original = stringResource(id = R.string.delete_task_confirm_msg, taskTitle)
        append(original)
        val start = original.indexOf(taskTitle)
        addStyle(
            style = SpanStyle(fontWeight = FontWeight.Bold),
            start = start,
            end = start + taskTitle.length
        )
    }

    DisplayAlertDialog(title = stringResource(id = R.string.delete_task_confirm_title),
        message = message,
        isDialogVisible = isDialogVisible,
        closeDialog = { isDialogVisible = false }) {
        onActionClicked(TaskAction.DELETE)
    }

    DeleteAction(onDeleteClicked = {
        isDialogVisible = true
    })
    UpdateAction(onUpdateClicked = onActionClicked)
}

@Composable
fun DeleteAction(
    onDeleteClicked: () -> Unit
) {
    IconButton(onClick = onDeleteClicked) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.delete_icon),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun UpdateAction(
    onUpdateClicked: (TaskAction) -> Unit
) {
    IconButton(onClick = { onUpdateClicked(TaskAction.INSERT_OR_UPDATE) }) {
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
    NewTaskAppBar(onBackClicked = {}, onActionClicked = {})
}

@Preview
@Composable
fun ExistingTaskAppBarPreview() {
    ExistingTaskAppBar("My Task", onCloseClicked = {}, onActionClicked = {})
}