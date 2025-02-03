package eu.wedgess.piholecontrol.presentation.statistics.tabs.upstreams.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchUpstreamDestinationsUseCase
import eu.wedgess.piholecontrol.presentation.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.presentation.common.model.LegendData
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.statistics.common.components.donutchart.model.DonutChartDataCollection
import eu.wedgess.piholecontrol.presentation.statistics.tabs.upstreams.UpstreamDestinationsContract
import eu.wedgess.piholecontrol.utils.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class UpstreamDestinationsViewModel @Inject constructor(
    fetchUpstreamDestinationsUseCase: FetchUpstreamDestinationsUseCase
) : EventDrivenViewModel<UpstreamDestinationsContract.Event>, ViewModel() {

    private val selectedIndexFlow = MutableStateFlow(-1)

    val uiResult =
        fetchUpstreamDestinationsUseCase()
            .combine(selectedIndexFlow) { destinationsResult, selectedIndex ->
                destinationsResult.getOrElse {
                    return@combine UIResult.Error(
                        ResultType.Error.WithTitleAndSubTitleAndRetry(
                            title = UiText.StringResource(R.string.upstream_destinations_error),
                            subTitle = UiText.DynamicString(it.message ?: "Unknown error"),
                            onRetry = fetchUpstreamDestinationsUseCase::refresh
                        )
                    )
                }.run {
                    return@combine UIResult.Loaded(
                        UpstreamDestinationsContract.UiState(
                            donutChartDataCollection = DonutChartDataCollection(this),
                            legendData = this.mapIndexed { index, serversChartData ->
                                LegendData(
                                    title = serversChartData.title,
                                    subTitle = "${
                                        DecimalFormat("#.#")
                                            .format(serversChartData.percentage)
                                    }%",
                                    isSelected = selectedIndex == index
                                )
                            }
                        )
                    )
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                UIResult.Loading(ResultType.Loading.WithTitle())
            )

    override fun onEvent(event: UpstreamDestinationsContract.Event) {
        when (event) {
            is UpstreamDestinationsContract.Event.OnLegendItemSelected -> selectedIndexFlow.update {
                event.index
            }
        }
    }
}
