package com.androidisland.todocompose.arch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest


@Composable
fun <E : MviEffect> CollectEffects(effect: Flow<E>, collector: (E) -> Unit) {
    LaunchedEffect(key1 = Unit) {
        effect.collectLatest(collector)
    }
}