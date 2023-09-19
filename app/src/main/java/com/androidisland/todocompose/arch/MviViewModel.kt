package com.androidisland.todocompose.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow


abstract class MviViewModel<S : MviState, A : MviAction, E : MviEffect> : ViewModel() {

    abstract val initialState: S

    val currentState: S
        get() = _uiState.value

    private val _uiState: MutableStateFlow<S> by lazy { MutableStateFlow(initialState) }
    val state = _uiState.asStateFlow()

    private val _action: MutableSharedFlow<A> = MutableSharedFlow()
    val action = _action.asSharedFlow()

    private val _effect: Channel<E> = Channel(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        collectActions()
    }

    private fun collectActions() {
        action.onEach {
            handleAction(it)
        }.launchIn(viewModelScope)
    }

    fun applyAction(action: A) {
        _action.tryEmit(action)
    }

    protected abstract suspend fun handleAction(action: A)

    protected fun setEffect(builder: () -> E) {
        _effect.trySend(builder())
    }

    protected fun setState(reducer: S.() -> S) {
        val newState = currentState.reducer()
        _uiState.value = newState
    }

}