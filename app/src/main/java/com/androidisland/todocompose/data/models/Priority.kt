package com.androidisland.todocompose.data.models

import androidx.compose.ui.graphics.Color
import com.androidisland.todocompose.ui.theme.HighPriorityColor
import com.androidisland.todocompose.ui.theme.LowPriorityColor
import com.androidisland.todocompose.ui.theme.MediumPriorityColor
import com.androidisland.todocompose.ui.theme.NonePriorityColor


enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}