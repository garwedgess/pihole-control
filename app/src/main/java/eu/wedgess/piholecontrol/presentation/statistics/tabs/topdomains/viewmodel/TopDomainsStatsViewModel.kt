package eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchTopQueriesUseCase
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.TopDomainsStatsContract
import eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.model.TopDomainsInfo
import eu.wedgess.piholecontrol.utils.UiText
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TopDomainsStatsViewModel @Inject constructor(
    fetchTopQueriesUseCase: FetchTopQueriesUseCase
) : ViewModel() {

    val uiResult = fetchTopQueriesUseCase()
        .map { result ->
            result.getOrElse {
                return@map UIResult.Error(
                    ResultType.Error.WithTitleAndSubTitle(
                        UiText.StringResource(R.string.top_domains_error),
                        UiText.DynamicString(it.message ?: "Unknown error")
                    )
                )
            }.run {
                return@map UIResult.Loaded(
                    TopDomainsStatsContract.UiState(
                        topPermitted = TopDomainsInfo(allowed),
                        topBlocked = TopDomainsInfo(blocked)
                    )
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UIResult.Loading(ResultType.Loading.WithTitle())
        )
}
