package eu.wedgess.mihole.ui.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.PiHoleRepository
import eu.wedgess.mihole.data.model.ResponseResult
import eu.wedgess.mihole.ui.base.RefreshableViewModel
import eu.wedgess.mihole.ui.dashboard.DashboardContract
import eu.wedgess.mihole.utils.UiText
import eu.wedgess.mihole.utils.extensions.handleError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: PiHoleRepository
) : RefreshableViewModel(repository), DashboardContract {

    private val _uiState: MutableStateFlow<DashboardContract.UiState> =
        MutableStateFlow(DashboardContract.UiState.initial())
    override val uiState: StateFlow<DashboardContract.UiState> = _uiState.asStateFlow()

    private val _effect: Channel<DashboardContract.Effect> = Channel(Channel.UNLIMITED)
    override val effect: Flow<DashboardContract.Effect> = _effect.receiveAsFlow()

    override fun onEvent(event: DashboardContract.Event) {
        when (event) {
            DashboardContract.Event.FetchSummary -> fetchStatistics()
            DashboardContract.Event.FetchQueriesOvertime -> fetchQueriesOverTime()
            DashboardContract.Event.FetchClientQueriesOvertime -> fetchClientQueriesOverTime()
            DashboardContract.Event.ListenForConnectionChanges -> listenForConnectionChange()
        }
    }

    private val statisticsErrorHandler = CoroutineExceptionHandler { _, throwable ->
        val errorMessage = throwable.message?.run {
            UiText.DynamicString(this)
        } ?: UiText.StringResource(R.string.all_error_msg_unknown)
        _uiState.update { it.summaryError(errorMessage) }
    }

    private val queriesOverTimeErrorHandler = CoroutineExceptionHandler { _, throwable ->
        val errorMessage = throwable.message?.run {
            UiText.DynamicString(this)
        } ?: UiText.StringResource(R.string.all_error_msg_unknown)
        _uiState.update { it.overtimeError(errorMessage) }
    }

    private val clientQueriesOverTimeErrorHandler = CoroutineExceptionHandler { _, throwable ->
        val errorMessage = throwable.message?.run {
            UiText.DynamicString(this)
        } ?: UiText.StringResource(R.string.all_error_msg_unknown)
        _uiState.update { it.overtimeError(errorMessage) }
    }

    private fun fetchStatistics() = viewModelScope.launch(statisticsErrorHandler) {
        when (val response = repository.fetchStatusSummary()) {
            is ResponseResult.Success -> _uiState.update { it.summary(response.data) }

            is ResponseResult.Error -> statisticsErrorHandler.handleException(
                this@launch.coroutineContext,
                response.handleError()
            )
        }
    }

    private fun fetchQueriesOverTime() = viewModelScope.launch(queriesOverTimeErrorHandler) {
        when (val response = repository.fetchOverTimeData()) {
            is ResponseResult.Success -> _uiState.update { it.overtime(response.data) }
            is ResponseResult.Error -> queriesOverTimeErrorHandler.handleException(
                this@launch.coroutineContext,
                response.handleError()
            )
        }
    }

    private fun fetchClientQueriesOverTime() =
        viewModelScope.launch(clientQueriesOverTimeErrorHandler) {
            when (val response = repository.fetchOverTimeDataClients()) {
                is ResponseResult.Success -> _uiState.update { it.clientOvertime(response.data) }
                is ResponseResult.Error -> clientQueriesOverTimeErrorHandler.handleException(
                    this@launch.coroutineContext,
                    response.handleError()
                )
            }
        }

    override fun onRefresh() {
        fetchStatistics()
        fetchQueriesOverTime()
        fetchClientQueriesOverTime()
    }
}