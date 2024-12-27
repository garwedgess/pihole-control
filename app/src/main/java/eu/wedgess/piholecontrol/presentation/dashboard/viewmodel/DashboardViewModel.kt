package eu.wedgess.piholecontrol.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.DashboardInfoEntity
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchDashboardInfoUseCase
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModel
import eu.wedgess.piholecontrol.presentation.base.SideEffectViewModelImpl
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.dashboard.DashboardContract
import eu.wedgess.piholecontrol.presentation.dashboard.extensions.toUiInfo
import eu.wedgess.piholecontrol.presentation.dashboard.model.DashboardInfo
import eu.wedgess.piholecontrol.utils.UiText
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    fetchDashboardInfoUseCase: FetchDashboardInfoUseCase
) : ViewModel(),
    SideEffectViewModel<DashboardContract.Effect> by SideEffectViewModelImpl() {

    private var showErrorMessage: Boolean = true

    val uiResult = fetchDashboardInfoUseCase()
        .map { result ->
            result.getOrElse { throwable ->
                return@map DashboardInfo.handleErrorThrowable(throwable)
            }.run {
                this.toUiInfo().toUiResult().also {
                    if (it !is UIResult.Error && showErrorMessage) {
                        handleCombinedErrors(this)
                    }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UIResult.Loading(ResultType.Loading.WithTitle())
        )

    private fun handleCombinedErrors(dashboardInfoEntity: DashboardInfoEntity) {
        val failures = mutableListOf<UiText>()
        dashboardInfoEntity.summaryResult.onFailure {
            failures.add(
                UiText.StringResourceWithArgs(
                    id = R.string.dashboard_summary_error,
                    it.message ?: ""
                )
            )
        }
        dashboardInfoEntity.queriesOverTimeResult.onFailure {
            failures.add(
                UiText.StringResourceWithArgs(
                    id = R.string.dashboard_queries_over_time_error,
                    it.message ?: ""
                )
            )
        }
        dashboardInfoEntity.clientQueriesOverTimeResult.onFailure {
            failures.add(
                UiText.StringResourceWithArgs(
                    id = R.string.dashboard_client_queries_over_time_error,
                    it.message ?: ""
                )
            )
        }
        if (failures.isNotEmpty()) {
            showErrorMessage = false
            viewModelScope.emitSideEffect(
                DashboardContract.Effect.ShowErrorSnackbar(failures)
            )
        }
    }
}
