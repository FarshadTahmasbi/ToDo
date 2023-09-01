package com.androidisland.todocompose.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.androidisland.todocompose.util.ActionEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val actionEventChannel: Channel<ActionEvent?>
) : ViewModel() {
    fun isValid(title: String, description: String): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }
}