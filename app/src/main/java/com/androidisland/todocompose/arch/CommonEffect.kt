package com.androidisland.todocompose.arch

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import com.androidisland.todocompose.enums.MessageType


sealed class CommonEffect : MviEffect {
    sealed class SnackBarEffect : CommonEffect() {
        abstract val message: String
        abstract val messageType: MessageType
        abstract val duration: SnackbarDuration
    }

    data class ShowSnackBar(
        override val message: String,
        override val messageType: MessageType = MessageType.INFO,
        override val duration: SnackbarDuration = SnackbarDuration.Short
    ) : SnackBarEffect()

    data class ShowSnackBarWithAction(
        override val message: String,
        override val messageType: MessageType = MessageType.INFO,
        override val duration: SnackbarDuration = SnackbarDuration.Short,
        val action: String,
        val onResult: (SnackbarResult) -> Unit
    ) : SnackBarEffect()

    abstract class FeatureEffect : CommonEffect()
}