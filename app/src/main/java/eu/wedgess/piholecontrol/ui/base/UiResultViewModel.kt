package eu.wedgess.piholecontrol.ui.base

import eu.wedgess.piholecontrol.ui.compose.UIResult
import kotlinx.coroutines.flow.StateFlow

interface UiResultViewModel<State> {
    val uiResult: StateFlow<UIResult<State>>
}