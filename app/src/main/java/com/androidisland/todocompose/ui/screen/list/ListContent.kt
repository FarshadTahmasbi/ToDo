package com.androidisland.todocompose.ui.screen.list

import android.os.Vibrator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androidisland.todocompose.R
import com.androidisland.todocompose.data.models.Priority
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.ext.clickableThrottleFirst
import com.androidisland.todocompose.ext.getSystemService
import com.androidisland.todocompose.ext.vibrateOneShot
import com.androidisland.todocompose.ui.common.DismissDirection2
import com.androidisland.todocompose.ui.common.DismissThreshold
import com.androidisland.todocompose.ui.common.DismissValue2
import com.androidisland.todocompose.ui.common.SwipeToDismiss2
import com.androidisland.todocompose.ui.theme.HighPriorityColor
import com.androidisland.todocompose.ui.theme.LowPriorityColor
import com.androidisland.todocompose.ui.theme.dimens
import com.androidisland.todocompose.ui.viewmodel.SharedViewModel
import com.androidisland.todocompose.util.Action
import kotlinx.coroutines.delay


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
            LoadingContent()
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
            ErrorContent(
                message = it.message ?: stringResource(id = R.string.message_general_error)
            )
        })

    } else {
        //Only idle happens
    }
}

@Composable
fun ListSuccessContent(
    tasks: List<ToDoTask>,
    onSwipeDismiss: (Action, ToDoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    contentPadding: PaddingValues
) {
    LazyColumn(contentPadding = contentPadding) {
        items(items = tasks, key = { task ->
            task.id
        }) { task ->

            val context = LocalContext.current
            val vibrator = remember {
                context.getSystemService<Vibrator>()
            }

            val vibrationDuration = 50L
            val visibilityAnimDuration = 300

            var dismissValue: DismissValue2? by remember {
                mutableStateOf(null)
            }

            var isDismissed: Boolean by remember {
                mutableStateOf(false)
            }

            //Check if item is dismissed, wait to finish animation, then invoke swipe dismiss
            if (isDismissed) {
                LaunchedEffect(key1 = Unit) {
                    //Wait for animation
                    delay(visibilityAnimDuration.toLong())
                    onSwipeDismiss(Action.DELETE, task)
                }
            }

            var isThresholdTouched: Boolean by remember {
                mutableStateOf(false)
            }

            if (isThresholdTouched) {
                LaunchedEffect(key1 = Unit) {
                    vibrator?.vibrateOneShot(vibrationDuration)
                }
            }

            val degrees by animateFloatAsState(
                targetValue = if (isThresholdTouched.not()) 0f else -45f, label = "Swipe Animation"
            )

            var isItemVisible by remember {
                mutableStateOf(false)
            }

            //Animate all added items
            LaunchedEffect(key1 = true) {
                isItemVisible = true
            }

            AnimatedVisibility(
                visible = isItemVisible && isDismissed.not(),
                enter = expandVertically(animationSpec = tween(durationMillis = visibilityAnimDuration)),
                exit = shrinkVertically(animationSpec = tween(durationMillis = visibilityAnimDuration))
            ) {
                SwipeToDismiss2(
                    setOf(DismissDirection2.RightToLeft, DismissDirection2.LeftToRight),
                    dismissThreshold = { size, density ->
                        with(density) {
                            DismissThreshold.Positional(size.height.toDp())
                        }
                    },
                    background = {
                        SwipeBackground(
                            modifier = Modifier.fillMaxSize(),
                            dismissValue = dismissValue,
                            degrees = degrees
                        )
                    },
                    dismissContent = {
                        ToDoTaskItem(
                            toDoTask = task, navigateToTaskScreen = navigateToTaskScreen
                        )
                    },
                    onDismissStateChange = {
                        dismissValue = it.value
                        isThresholdTouched = it.isThresholdTouched
                        if (it.isDismissed) {
                            when (it.value) {
                                DismissValue2.DismissedToLeft -> {
                                    isDismissed = true
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
}

@Composable
fun SwipeBackground(
    modifier: Modifier = Modifier,
    dismissValue: DismissValue2?,
    degrees: Float
) {
    val backgroundColor =
        if (dismissValue == DismissValue2.DismissedToLeft) HighPriorityColor else LowPriorityColor
    val contentAlignment =
        if (dismissValue == DismissValue2.DismissedToLeft) Alignment.CenterEnd else Alignment.CenterStart
    val icon =
        if (dismissValue == DismissValue2.DismissedToLeft) Icons.Filled.Delete else Icons.Filled.Edit
    Box(
        modifier = modifier.then(
            Modifier
                .background(backgroundColor)
                .padding(horizontal = MaterialTheme.dimens.xxLargePadding)
        ),
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

@Preview
@Composable
fun SwipeToLeftBackground() {
    SwipeBackground(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        dismissValue = DismissValue2.DismissedToLeft,
        degrees = 0f
    )
}

@Preview
@Composable
fun SwipeToRightBackground() {
    SwipeBackground(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        dismissValue = DismissValue2.DismissedToRight,
        degrees = 0f
    )
}