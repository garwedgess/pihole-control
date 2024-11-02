package eu.wedgess.mihole.ui.base

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UiStateViewModelImpl<State>(initialUiState: State) : UiStateViewModel<State> {
    private val _uiState = MutableStateFlow(initialUiState)
    override val uiState: StateFlow<State> = _uiState.asStateFlow()

    override fun updateUiState(newUiState: State) {
        _uiState.update { newUiState }
    }

    override fun updateUiState(block: State.() -> State) {
        _uiState.update(block)
    }
}