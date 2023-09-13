package com.androidisland.todocompose.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor() : ViewModel() {
    fun isValid(title: String, description: String): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }
}