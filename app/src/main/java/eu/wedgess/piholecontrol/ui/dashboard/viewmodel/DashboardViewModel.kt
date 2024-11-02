package eu.wedgess.piholecontrol.ui.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.ui.compose.ResultType
import eu.wedgess.piholecontrol.ui.compose.UIResult
import eu.wedgess.piholecontrol.ui.dashboard.controller.DashboardController
import eu.wedgess.piholecontrol.utils.UiText
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    controller: DashboardController
) : ViewModel() {

    val uiResult = controller.dashboardInfo()
        .map { result ->
            result.getOrElse { throwable ->
                Timber.e("Failed to fetch dashboard info", throwable)
                return@map UIResult.Error(
                    ResultType.Error.WithTitleAndSubTitle(
                        title = UiText.DynamicString("Failed to fetch dashboard info"),
                        subTitle = UiText.DynamicString(throwable.message?.takeIf { it.isNotBlank() } ?: "Unknown error")
                    )
                )
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