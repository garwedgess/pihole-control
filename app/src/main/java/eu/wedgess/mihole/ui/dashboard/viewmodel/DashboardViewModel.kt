package eu.wedgess.mihole.ui.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.ui.compose.ResultType
import eu.wedgess.mihole.ui.compose.UIResult
import eu.wedgess.mihole.ui.dashboard.controller.DashboardController
import eu.wedgess.mihole.utils.UiText
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    controller: DashboardController
) : ViewModel() {

    val uiResult = controller.dashboardInfo()
        .map { result ->
            result.getOrElse {
                return@map UIResult.Error(ResultType.Error.WithTitle(UiText.DynamicString("Failed to fetch dashboard info")))
            }.run {
                return@map this.toUiResult()
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UIResult.Loading(ResultType.Loading.WithTitle())
        )
}