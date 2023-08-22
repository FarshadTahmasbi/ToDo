package com.androidisland.todocompose.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.androidisland.todocompose.data.models.Priority
import com.androidisland.todocompose.ui.theme.dimens


@Composable
fun PriorityItem(priority: Priority) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(MaterialTheme.dimens.priorityIndicatorSize)) {
            drawCircle(color = priority.color)
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
    PriorityItem(priority = Priority.HIGH)
}