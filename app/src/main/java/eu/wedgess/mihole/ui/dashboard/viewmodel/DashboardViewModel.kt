package eu.wedgess.mihole.ui.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.data.PiHoleRepository
import eu.wedgess.mihole.data.model.ResponseResult
import eu.wedgess.mihole.ui.dashboard.DashboardContract
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
) : ViewModel(), DashboardContract {

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
        }
    }

    private val statisticsErrorHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.update { it.summaryError(throwable.message ?: "Unknown error") }
    }

    private val queriesOverTimeErrorHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.update { it.overtimeError(throwable.message ?: "Unknown error") }
    }

    private val clientQueriesOverTimeErrorHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.update { it.overtimeError(throwable.message ?: "Unknown error") }
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

    private fun fetchClientQueriesOverTime() = viewModelScope.launch(clientQueriesOverTimeErrorHandler) {
        when (val response = repository.fetchOverTimeDataClients()) {
            is ResponseResult.Success -> _uiState.update { it.clientOvertime(response.data) }
            is ResponseResult.Error -> clientQueriesOverTimeErrorHandler.handleException(
                this@launch.coroutineContext,
                response.handleError()
            )
        }
    }

    private fun <E> ResponseResult.Error<E>.handleError(): Throwable =
        when (this) {
            is ResponseResult.Error.Api -> Throwable(this.body as String? ?: "Unknown error")
            is ResponseResult.Error.Network -> this.exception
            is ResponseResult.Error.Serialization -> this.exception
            is ResponseResult.Error.Unknown -> this.exception
        }


}