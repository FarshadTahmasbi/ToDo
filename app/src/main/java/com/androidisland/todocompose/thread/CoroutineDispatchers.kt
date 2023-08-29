package com.androidisland.todocompose.thread

import kotlinx.coroutines.CoroutineDispatcher


data class CoroutineDispatchers(
    val main: CoroutineDispatcher,
    val io: CoroutineDispatcher
)