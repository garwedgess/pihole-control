package eu.wedgess.piholecontrol.ui.statserver.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.ui.compose.ResultType
import eu.wedgess.piholecontrol.ui.compose.UIResult
import eu.wedgess.piholecontrol.ui.statserver.controller.ServerStatsController
import eu.wedgess.piholecontrol.utils.UiText
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ServerStatsViewModel @Inject constructor(
    controller: ServerStatsController
) : ViewModel() {

    val uiResult = controller.forwardDestinations()
        .map { result ->
            result.getOrElse {
                return@map UIResult.Error(ResultType.Error.WithTitleAndSubTitle(
                    UiText.DynamicString("Failed to fetch forward destinations"),
                    UiText.DynamicString(it.message ?: "Unknown error"))
                )
            }.run {
                return@map UIResult.Loaded(this)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UIResult.Loading(ResultType.Loading.WithTitle())
        )
}