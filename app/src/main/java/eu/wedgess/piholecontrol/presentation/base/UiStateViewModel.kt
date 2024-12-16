package eu.wedgess.piholecontrol.presentation.base

import kotlinx.coroutines.flow.StateFlow

interface UiStateViewModel<State> {
    val uiState: StateFlow<State>

    fun updateUiState(newUiState: State)

    fun updateUiState(block: State.() -> State)
}
