package com.androidisland.todocompose.arch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow


abstract class MviViewModel<S : State, A : Action, E : Effect>(
    protected val savedStateHandle: SavedStateHandle
) : ViewModel() {

    abstract val initialState: S

    val currentState: S
        get() = _uiState.value

    private val _uiState: MutableStateFlow<S> by lazy { MutableStateFlow(initialState) }
    val state = _uiState.asStateFlow()

    private val _action: MutableSharedFlow<A> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val _effect: Channel<E?> = Channel(Channel.BUFFERED)

    /**
     * Use this flow to collect all side effects.
     *
     * Note: If this flow is combined with Jetpack Compose, you need to collect it as a state,
     * Then pass it as key argument of the [LaunchEffect] composable function and consume it there.
     */
    val effect = _effect.receiveAsFlow()

    init {
        collectActions()
    }

    private fun collectActions() {
        _action.onEach {
            _uiState.value = reduce(currentState, it)
        }.launchIn(viewModelScope)
    }

    protected fun dispatchAction(action: A) {
        _action.tryEmit(action)
    }

    protected abstract suspend fun reduce(currentState: S, action: A): S

    protected fun dispatchEffect(builder: () -> E) {
        _effect.trySend(builder())
        _effect.trySend(null)
    }
}