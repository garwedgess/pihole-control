package eu.wedgess.piholecontrol.presentation.statistics.tabs.querytypes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchQueryTypesUseCase
import eu.wedgess.piholecontrol.presentation.base.EventDrivenViewModel
import eu.wedgess.piholecontrol.presentation.common.model.LegendData
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.statistics.tabs.querytypes.QueryTypesContract
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model.DonutChartDataCollection
import eu.wedgess.piholecontrol.utils.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class QueryTypesViewModel @Inject constructor(
    fetchQueryTypesUseCase: FetchQueryTypesUseCase
) : EventDrivenViewModel<QueryTypesContract.Event>, ViewModel() {

    private val selectedIndexFlow = MutableStateFlow(-1)

    val uiResult =
        fetchQueryTypesUseCase().combine(selectedIndexFlow) { queryTypesResult, selectedIndex ->
            queryTypesResult.getOrElse {
                return@combine UIResult.Error(
                    ResultType.Error.WithTitleAndSubTitle(
                        UiText.StringResource(R.string.query_types_error),
                        UiText.DynamicString(it.message ?: "Unknown error")
                    )
                )
            }.run {
                return@combine UIResult.Loaded(
                    QueryTypesContract.UiState(
                        donutChartDataCollection = DonutChartDataCollection(this),
                        legendData = this.mapIndexed { index, queryTypeChartData ->
                            LegendData(
                                title = queryTypeChartData.title,
                                subTitle = "${queryTypeChartData.percentage}%",
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

    override fun onEvent(event: QueryTypesContract.Event) {
        when (event) {
            is QueryTypesContract.Event.OnLegendItemSelected -> selectedIndexFlow.update {
                event.index
            }
        }
    }
}
