package com.androidisland.todocompose.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androidisland.todocompose.R
import com.androidisland.todocompose.data.models.Priority
import com.androidisland.todocompose.ui.theme.alphaDisabled
import com.androidisland.todocompose.ui.theme.dimens


@Composable
fun PriorityDropDown(
    priority: Priority, onPrioritySelected: (Priority) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val angle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "Drop down arrow animation"
    )

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { expanded = true }
        .background(color = MaterialTheme.colorScheme.surface, shape = RectangleShape)
        .border(
            width = 1.dp, color = MaterialTheme.colorScheme.surface.copy(alpha = alphaDisabled)
        )
        .padding(MaterialTheme.dimens.smallPadding),
        verticalAlignment = Alignment.CenterVertically) {
        Canvas(
            modifier = Modifier
                .size(MaterialTheme.dimens.priorityIndicatorSize)
                .weight(1f)
        ) {
            drawCircle(color = priority.color)
        }
        Text(
            modifier = Modifier
                .weight(8f)
                .padding(
                    start = MaterialTheme.dimens.smallPadding,
                    end = MaterialTheme.dimens.smallPadding
                ), text = priority.title, style = MaterialTheme.typography.titleMedium
        )
        IconButton(modifier = Modifier
            .rotate(angle)
            .weight(1f),
            onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(R.string.drop_down_arrow)
            )
        }
        DropdownMenu(modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = {
                PriorityItem(priority = Priority.LOW)
            }, onClick = {
                expanded = false
                onPrioritySelected(Priority.LOW)
            })
            DropdownMenuItem(text = {
                PriorityItem(priority = Priority.MEDIUM)
            }, onClick = {
                expanded = false
                onPrioritySelected(Priority.MEDIUM)
            })
            DropdownMenuItem(text = {
                PriorityItem(priority = Priority.HIGH)
            }, onClick = {
                expanded = false
                onPrioritySelected(Priority.HIGH)
            })
        }
    }
}

@Preview
@Composable
fun PriorityDropDownPreview() {
    PriorityDropDown(priority = Priority.LOW, onPrioritySelected = {})
}