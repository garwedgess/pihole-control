package eu.wedgess.piholecontrol.presentation.dashboard

import eu.wedgess.piholecontrol.domain.model.SummaryEntity
import eu.wedgess.piholecontrol.presentation.dashboard.model.LineChartInfo
import eu.wedgess.piholecontrol.presentation.dashboard.model.OverTimeLineChartInfo
import eu.wedgess.piholecontrol.utils.UiText

interface DashboardContract {

    data class UiState(
        val summary: SummaryEntity?,
        val overtimeLineChart: OverTimeLineChartInfo?,
        val clientQueriesOverTime: List<LineChartInfo.ClientsOverLineChart>?
    )

    sealed interface Effect {
        data class ShowErrorSnackbar(val errorMessages: List<UiText>) : Effect
    }

    sealed interface Event {
        data object FetchDashboardInfo : Event
        data class OnRetry(val action: () -> Unit) : Event
    }
}