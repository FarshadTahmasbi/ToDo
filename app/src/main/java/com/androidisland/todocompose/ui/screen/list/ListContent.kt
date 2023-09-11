package com.androidisland.todocompose.ui.screen.list

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.androidisland.todocompose.R
import com.androidisland.todocompose.data.models.Priority
import com.androidisland.todocompose.data.models.ToDoTask
import com.androidisland.todocompose.ext.clickableThrottleFirst
import com.androidisland.todocompose.ui.theme.HighPriorityColor
import com.androidisland.todocompose.ui.theme.dimens
import com.androidisland.todocompose.ui.viewmodel.SharedViewModel
import com.androidisland.todocompose.util.Action
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


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
                DisplayTasks(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayTasks(
    tasks: List<ToDoTask>,
    onSwipeDismiss: (Action, ToDoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    contentPadding: PaddingValues
) {
    LazyColumn(contentPadding = contentPadding) {
        items(items = tasks, key = { task ->
            task.id
        }) { task ->
            Log.d("test123", "progress->")

//            val dismissState = rememberDismissState(
//                confirmValueChange = {
//                    Log.d("test123", "STATE CHANGE -> $it")
//                    true
//                },
//                positionalThreshold = {
//                    160.dp.toPx()
//                }
//            )
//            val dismissDirection = dismissState.dismissDirection
//            val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
//            Log.d("test123", "progress-> ${dismissState.progress}")
//            if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
//                onSwipeDismiss(Action.DELETE, task)
////                LaunchedEffect(key1 = Unit) {
////                    scope.launch {
////                        dismissState.reset()
////                    }
////                }
//
//            }
//
//            val degrees by animateFloatAsState(
//                targetValue = if (dismissState.targetValue == DismissValue.Default) 0f else -45f,
//                label = "Swipe Animation"
//            )
//            SwipeToDismiss(
//                state = dismissState,
//                background = {
//                    SwipeBackground(degrees = degrees)
//                },
//                dismissContent = {
//                    ToDoTaskItem(
//                        toDoTask = task, navigateToTaskScreen = navigateToTaskScreen
//                    )
//                },
//                directions = setOf(DismissDirection.EndToStart)
//            )

            DraggableItem(task = task, navigateToTaskScreen = navigateToTaskScreen)
        }
    }
}

@Composable
fun DraggableItem(
    task: ToDoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
//    val draggableState = rememberDraggableState(onDelta = {
//        Log.d("test123", "drag delta -> $it")
//    })
    val coroutineScope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }


    Box(
        modifier = Modifier
            .offset {
                IntOffset(offsetX.value.roundToInt(), 0)
            }
            .draggable(
                state = rememberDraggableState { delta ->
                    Log.d("test123", "DragDelta -> $delta, OffsetX->${offsetX.value}")
                    coroutineScope.launch {
                        offsetX.snapTo(offsetX.value + delta)
                    }
                },
                orientation = Orientation.Horizontal,
                onDragStopped = {
                    coroutineScope.launch {
                        offsetX.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(
                                durationMillis = 300,
                                delayMillis = 0
                            ),
//                            animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy,
//                                stiffness = Spring.StiffnessVeryLow,
//                                visibilityThreshold = null),
                            block = { Log.d("test123", "===>$it") }
                        )
                    }
                }
            )
    ) {
        ToDoTaskItem(toDoTask = task, navigateToTaskScreen = navigateToTaskScreen)
    }
}

@Composable
fun SwipeBackground(degrees: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HighPriorityColor)
            .padding(horizontal = MaterialTheme.dimens.xxLargePadding),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            modifier = Modifier.rotate(degrees = degrees),
            imageVector = Icons.Filled.Delete,
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