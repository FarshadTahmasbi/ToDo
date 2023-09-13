package com.androidisland.todocompose.ui.screen.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.androidisland.todocompose.R
import com.androidisland.todocompose.data.models.Priority
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.ext.clickableThrottleFirst
import com.androidisland.todocompose.ui.common.DismissDirection2
import com.androidisland.todocompose.ui.common.DismissThreshold
import com.androidisland.todocompose.ui.common.DismissValue2
import com.androidisland.todocompose.ui.common.SwipeToDismiss2
import com.androidisland.todocompose.ui.theme.HighPriorityColor
import com.androidisland.todocompose.ui.theme.LowPriorityColor
import com.androidisland.todocompose.ui.theme.dimens
import com.androidisland.todocompose.ui.viewmodel.SharedViewModel
import com.androidisland.todocompose.util.Action


@Composable
fun ListContent(
    sortState: Priority?,
    isInSearchMode: Boolean,
    sharedViewModel: SharedViewModel,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    onSwipeDismiss: (Action, ToDoTask) -> Unit,
    contentPadding: PaddingValues
) {
    if (sortState != null) {
        val tasksResource by when {
            isInSearchMode || sortState == Priority.NONE -> sharedViewModel.queriedTasks.collectAsState()
            sortState == Priority.LOW -> sharedViewModel.lowPriorityTasks.collectAsState()
            sortState == Priority.HIGH -> sharedViewModel.highPriorityTasks.collectAsState()
            else -> throw RuntimeException("Unexpected sort state: $sortState")
        }

        tasksResource.fold(onLoading = {
            //TODO compose it!
        }, onSuccess = { data ->
            if (data.isEmpty()) {
                EmptyContent()
            } else {
                ListSuccessContent(
                    tasks = data,
                    onSwipeDismiss = onSwipeDismiss,
                    navigateToTaskScreen = navigateToTaskScreen,
                    contentPadding = contentPadding
                )
            }
        }, onError = {
            //TODO compose it!
        })

    } else {
        //Only idle happens
        //TODO compose it!
    }
}

@Composable
fun ListSuccessContent(
    tasks: List<ToDoTask>,
    onSwipeDismiss: (Action, ToDoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    contentPadding: PaddingValues
) {
    //TODO animation, swipe to edit!
    LazyColumn(contentPadding = contentPadding) {
        items(items = tasks, key = { task ->
            task.id
        }) { task ->

            var dismissValue: DismissValue2? by remember {
                mutableStateOf(null)
            }

            var isSwiped: Boolean by remember {
                mutableStateOf(false)
            }

            val degrees by animateFloatAsState(
                targetValue = if (isSwiped.not()) 0f else -45f, label = "Swipe Animation"
            )

            SwipeToDismiss2(
                setOf(DismissDirection2.RightToLeft, DismissDirection2.LeftToRight),
                dismissThreshold = { size, density ->
                    with(density) {
                        DismissThreshold.Positional(size.height.toDp())
                    }
                },
                background = {
                    SwipeBackground(
                        dismissValue = dismissValue, degrees = degrees
                    )
                },
                dismissContent = {
                    ToDoTaskItem(
                        toDoTask = task, navigateToTaskScreen = navigateToTaskScreen
                    )
                },
                onDismissStateChange = {
                    dismissValue = it.value
                    isSwiped = it.isThresholdTouched
                    if (it.isDismissed) {
                        when (it.value) {
                            DismissValue2.DismissedToLeft -> {
                                onSwipeDismiss(Action.DELETE, task)
                            }

                            DismissValue2.DismissedToRight -> {
                                navigateToTaskScreen(task.id)
                            }

                            else -> Unit
                        }
                    }
                })
        }
    }
}

@Composable
fun SwipeBackground(
    dismissValue: DismissValue2?, degrees: Float
) {
    val backgroundColor =
        if (dismissValue == DismissValue2.DismissedToLeft) HighPriorityColor else LowPriorityColor
    val contentAlignment =
        if (dismissValue == DismissValue2.DismissedToLeft) Alignment.CenterEnd else Alignment.CenterStart
    val icon =
        if (dismissValue == DismissValue2.DismissedToLeft) Icons.Filled.Delete else Icons.Filled.Edit
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = MaterialTheme.dimens.xxLargePadding),
        contentAlignment = contentAlignment
    ) {
        Icon(
            modifier = Modifier.rotate(degrees = degrees),
            imageVector = icon,
            contentDescription = stringResource(id = R.string.delete_icon),
            tint = Color.White
        )
    }
}

@Composable
fun ToDoTaskItem(
    toDoTask: ToDoTask, navigateToTaskScreen: (taskId: Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickableThrottleFirst { navigateToTaskScreen(toDoTask.id) },
        shape = RectangleShape,
        shadowElevation = MaterialTheme.dimens.elevationSmall,
    ) {
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