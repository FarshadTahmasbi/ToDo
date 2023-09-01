package com.androidisland.todocompose.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.androidisland.todocompose.R
import com.androidisland.todocompose.data.models.Priority
import com.androidisland.todocompose.ui.theme.dimens


@Composable
fun PriorityItem(
    priority: Priority,
    isSelected: Boolean = false
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box {
            Canvas(modifier = Modifier.size(MaterialTheme.dimens.priorityIndicatorSize)) {
                drawCircle(color = priority.color)
            }
            if (isSelected) {
                Icon(
                    modifier = Modifier
                        .size(MaterialTheme.dimens.priorityIndicatorSize * 0.7f)
                        .align(Alignment.Center),
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(id = R.string.update_icon),
                    tint = Color.White
                )
            }
        }
        Text(
            modifier = Modifier.padding(start = MaterialTheme.dimens.mediumPadding),
            text = priority.title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
fun PriorityItemPreview() {
    PriorityItem(
        priority = Priority.HIGH,
        isSelected = true
    )
}