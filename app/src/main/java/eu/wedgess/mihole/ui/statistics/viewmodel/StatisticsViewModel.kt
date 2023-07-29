package eu.wedgess.mihole.ui.statistics.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.PiHoleRepository
import eu.wedgess.mihole.data.model.ResponseResult
import eu.wedgess.mihole.ui.base.RefreshableViewModel
import eu.wedgess.mihole.ui.statistics.StatisticsContract
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
class StatisticsViewModel @Inject constructor(
    private val repository: PiHoleRepository
) : RefreshableViewModel(repository), StatisticsContract {

    private val _uiState: MutableStateFlow<StatisticsContract.UiState> =
        MutableStateFlow(StatisticsContract.UiState.initial())
    override val uiState: StateFlow<StatisticsContract.UiState> = _uiState.asStateFlow()

    private val _effect: Channel<StatisticsContract.Effect> = Channel(Channel.UNLIMITED)
    override val effect: Flow<StatisticsContract.Effect> = _effect.receiveAsFlow()

    override fun onEvent(event: StatisticsContract.Event) {
        when (event) {
            StatisticsContract.Event.FetchStatistics -> fetchStatistics()
            StatisticsContract.Event.ListenForConnectionChanges -> listenForConnectionChange()
        }
    }

    private val statisticsErrorHandler = CoroutineExceptionHandler { _, throwable ->
        val errorMessage = throwable.message?.run {
            UiText.DynamicString(this)
        } ?: UiText.StringResource(R.string.all_error_msg_unknown)
        _uiState.update { it.statisticsError(errorMessage) }
    }

    private fun fetchStatistics() = viewModelScope.launch(statisticsErrorHandler) {
        when (val response = repository.fetchStatistics()) {
            is ResponseResult.Success -> _uiState.update { it.statistics(response.data) }

            is ResponseResult.Error -> statisticsErrorHandler.handleException(
                this@launch.coroutineContext,
                response.handleError()
            )
        }
    }

    override fun onRefresh() {
        fetchStatistics()
    }
}