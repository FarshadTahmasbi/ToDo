package com.androidisland.todocompose.data.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.androidisland.todocompose.ui.theme.HighPriorityColor
import com.androidisland.todocompose.ui.theme.LowPriorityColor
import com.androidisland.todocompose.ui.theme.MediumPriorityColor
import com.androidisland.todocompose.ui.theme.NonePriorityColor


enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor);

    val title: String
        get() = name.lowercase().capitalize(Locale.current)
}