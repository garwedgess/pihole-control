package eu.wedgess.mihole.ui.base

import kotlinx.coroutines.flow.StateFlow

interface UiStateViewModel<State> {
    val uiState: StateFlow<State>

    fun updateUiState(newUiState: State)

    fun updateUiState(block: State.() -> State)
}