package com.treemiddle.photoexplorer.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<Event : ViewEvent, UiState : ViewState, Effect : ViewSideEffect> :
    ViewModel() {
    val viewState: StateFlow<UiState> get() = _viewState
    val effect get() = _effect.receiveAsFlow()

    private val initialState: UiState by lazy {
        setInitialState()
    }

    private val _viewState: MutableStateFlow<UiState> by lazy {
        MutableStateFlow(value = initialState)
    }

    private val _effect: Channel<Effect> = Channel()

    abstract fun setInitialState(): UiState
    abstract fun handleEvent(event: Event)

    protected fun setState(reducer: UiState.() -> UiState) {
        _viewState.update {
            it.reducer()
        }
    }

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(element = effectValue) }
    }
}
