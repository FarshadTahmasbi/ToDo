package com.androidisland.todocompose.ext

import com.androidisland.todocompose.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


fun <T> Flow<T>.toResourceStateFlow(scope: CoroutineScope) =
    MutableStateFlow<Resource<T>>(Resource.Loading).let { stateFlow ->
        catch {
            stateFlow.value = Resource.Error(it)
        }.onEach {
            stateFlow.value = Resource.Success(it)
        }.launchIn(scope)
        stateFlow.asStateFlow()
    }