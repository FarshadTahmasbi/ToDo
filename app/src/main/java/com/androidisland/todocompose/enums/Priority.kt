package com.androidisland.todocompose.enums

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.androidisland.todocompose.common.theme.HighPriorityColor
import com.androidisland.todocompose.common.theme.LowPriorityColor
import com.androidisland.todocompose.common.theme.MediumPriorityColor
import com.androidisland.todocompose.common.theme.NonePriorityColor


enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor);

    val title: String
        get() = name.lowercase().capitalize(Locale.current)
}