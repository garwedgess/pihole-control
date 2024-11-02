package eu.wedgess.mihole.ui.dashboard

import eu.wedgess.mihole.data.model.responses.PiHoleClientsOverTimeData
import eu.wedgess.mihole.data.model.responses.PiHoleOverTimeData
import eu.wedgess.mihole.data.model.responses.PiHoleSummary
import eu.wedgess.mihole.utils.UiText

interface DashboardContract {

    data class UiState(
        val summary: PiHoleSummary?,
        val queriesOverTime: PiHoleOverTimeData?,
        val clientQueriesOverTime: PiHoleClientsOverTimeData?
    )

    sealed interface Effect {
        data class ShowErrorSnackbar(val msg: UiText, val retryAction: () -> Unit) : Effect
    }

    sealed interface Event {
        data object FetchDashboardInfo : Event
        data class OnRetry(val action: () -> Unit) : Event
    }
}