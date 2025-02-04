package eu.wedgess.piholecontrol.presentation.statistics.tabs.topclients.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchTopClientsUseCase
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.statistics.tabs.topclients.TopClientStatsContract
import eu.wedgess.piholecontrol.utils.UiText
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TopClientsStatsViewModel @Inject constructor(
    fetchTopClientsUseCase: FetchTopClientsUseCase
) : ViewModel() {

    val uiResult = fetchTopClientsUseCase()
        .map { result ->
            result.getOrElse {
                return@map UIResult.Error(
                    ResultType.Error.WithTitleAndSubTitleAndRetry(
                        title = UiText.StringResource(R.string.top_clients_error),
                        subTitle = UiText.DynamicString(it.message ?: "Unknown error"),
                        onRetry = fetchTopClientsUseCase::refresh
                    )
                )
            }.run {
                return@map UIResult.Loaded(TopClientStatsContract.UiState(topQueries = this))
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UIResult.Loading(ResultType.Loading.WithTitle())
        )
}
