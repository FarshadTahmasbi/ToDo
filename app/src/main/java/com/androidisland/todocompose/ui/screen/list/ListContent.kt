package com.androidisland.todocompose.ui.screen.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.androidisland.todocompose.data.models.Priority
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.ui.theme.dimens
import com.androidisland.todocompose.util.Resource


@Composable
fun ListContent(
    tasksResource: Resource<List<ToDoTask>>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    contentPadding: PaddingValues
) {
    when (tasksResource) {
        is Resource.Idle -> {

        }

        is Resource.Loading -> {
        }

        is Resource.Success -> {
            if (tasksResource.data.isEmpty()) {
                EmptyContent()
            } else {
                DisplayTasks(
                    tasks = tasksResource.data,
                    navigateToTaskScreen = navigateToTaskScreen,
                    contentPadding = contentPadding
                )
            }
        }

        is Resource.Error -> {

        }
    }
}

@Composable
fun DisplayTasks(
    tasks: List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    contentPadding: PaddingValues
) {
    LazyColumn(contentPadding = contentPadding) {
        items(items = tasks,
            key = { task ->
                task.id
            }) { task ->
            ToDoTaskItem(
                toDoTask = task,
                navigateToTaskScreen = navigateToTaskScreen
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoTaskItem(
    toDoTask: ToDoTask, navigateToTaskScreen: (taskId: Int) -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        shadowElevation = MaterialTheme.dimens.elevationSmall,
        onClick = {
            navigateToTaskScreen(toDoTask.id)
        }) {
        Column(modifier = Modifier.padding(MaterialTheme.dimens.largePadding)) {
            Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                Text(
                    modifier = Modifier.weight(9f),
                    text = toDoTask.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    contentAlignment = Alignment.TopEnd,
                ) {
                    Canvas(
                        modifier = Modifier.requiredSize(MaterialTheme.dimens.priorityIndicatorSize)
                    ) {
                        drawCircle(color = toDoTask.priority.color)
                    }
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = toDoTask.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
//                textAlign = TextAlign.Justify
            )
        }
    }
}


@Preview
@Composable
fun ToDoTaskItemPreview() {
    ToDoTaskItem(toDoTask = ToDoTask(
        id = 1,
        title = "My task",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        priority = Priority.LOW
    ), navigateToTaskScreen = {})
}