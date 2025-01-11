package eu.wedgess.piholecontrol.presentation.statistics.tabs.destinations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchForwardDestinationsUseCase
import eu.wedgess.piholecontrol.presentation.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.presentation.common.model.LegendData
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.statistics.tabs.destinations.ForwardDestinationsContract
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model.DonutChartDataCollection
import eu.wedgess.piholecontrol.utils.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class ForwardDestinationsViewModel @Inject constructor(
    fetchForwardDestinationsUseCase: FetchForwardDestinationsUseCase
) : EventDrivenViewModel<ForwardDestinationsContract.Event>, ViewModel() {

    private val selectedIndexFlow = MutableStateFlow(-1)

    val uiResult =
        fetchForwardDestinationsUseCase()
            .combine(selectedIndexFlow) { destinationsResult, selectedIndex ->
                destinationsResult.getOrElse {
                    return@combine UIResult.Error(
                        ResultType.Error.WithTitleAndSubTitleAndRetry(
                            title = UiText.StringResource(R.string.forward_destinations_error),
                            subTitle = UiText.DynamicString(it.message ?: "Unknown error"),
                            onRetry = fetchForwardDestinationsUseCase::refresh
                        )
                    )
                }.run {
                    return@combine UIResult.Loaded(
                        ForwardDestinationsContract.UiState(
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

    override fun onEvent(event: ForwardDestinationsContract.Event) {
        when (event) {
            is ForwardDestinationsContract.Event.OnLegendItemSelected -> selectedIndexFlow.update {
                event.index
            }
        }
    }
}
