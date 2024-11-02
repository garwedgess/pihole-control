package eu.wedgess.mihole.ui.base

import eu.wedgess.mihole.ui.compose.UIResult
import kotlinx.coroutines.flow.StateFlow

interface UiResultViewModel<State> {
    val uiResult: StateFlow<UIResult<State>>
}