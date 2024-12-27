package eu.wedgess.piholecontrol.presentation.statistics.tabs.destinations

import eu.wedgess.piholecontrol.presentation.common.model.LegendData
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model.DonutChartDataCollection

interface ForwardDestinationsContract {

    data class UiState(
        val donutChartDataCollection: DonutChartDataCollection,
        val legendData: List<LegendData>
    ) {
        companion object {
            fun initial() = UiState(
                donutChartDataCollection = DonutChartDataCollection(emptyList()),
                legendData = emptyList()
            )
        }
    }

    sealed interface Event {
        data class OnLegendItemSelected(val index: Int) : Event
    }
}
