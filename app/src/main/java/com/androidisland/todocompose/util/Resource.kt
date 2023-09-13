package com.androidisland.todocompose.util


sealed class Resource<out T> {
    object Idle : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val error: Throwable) : Resource<Nothing>()

    fun getOrNull(): T? = if (this is Success) data else null

    fun isIdle() = this is Idle
    fun isLoading() = this is Loading
    fun isSuccess() = this is Success

    fun isError() = this is Error


    inline fun fold(
        onIdle: () -> Unit = {},
        onLoading: () -> Unit = {},
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        when (this) {
            is Idle -> onIdle()
            is Loading -> onLoading()
            is Success -> onSuccess(data)
            is Error -> onError(error)
        }
    }
}