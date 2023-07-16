package eu.wedgess.mihole.ui.dashboard

import eu.wedgess.mihole.data.model.PiHoleClientsOverTimeData
import eu.wedgess.mihole.data.model.PiHoleOverTimeData
import eu.wedgess.mihole.data.model.PiHoleSummary
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel
import eu.wedgess.mihole.utils.UiText

interface DashboardContract :
    UnidirectionalViewModel<DashboardContract.UiState, DashboardContract.Event, DashboardContract.Effect> {

    data class UiState(
        val summary: UiResult<PiHoleSummary>,
        val queriesOverTime: UiResult<PiHoleOverTimeData>,
        val clientQueriesOverTime: UiResult<PiHoleClientsOverTimeData>
    ) {

        fun summary(statusSummary: PiHoleSummary): UiState =
            this.copy(summary = UiResult.Success(statusSummary))

        fun summaryError(errorMessage: UiText): UiState =
            this.copy(summary = UiResult.Error(errorMessage))

        fun overtime(queriesOverTime: PiHoleOverTimeData): UiState =
            this.copy(queriesOverTime = UiResult.Success(queriesOverTime))

        fun overtimeError(errorMessage: UiText): UiState =
            this.copy(queriesOverTime = UiResult.Error(errorMessage))

        fun clientOvertime(queriesOverTime: PiHoleClientsOverTimeData): UiState =
            this.copy(clientQueriesOverTime = UiResult.Success(queriesOverTime))

        fun clientOvertimeError(errorMessage: UiText): UiState =
            this.copy(clientQueriesOverTime = UiResult.Error(errorMessage))

        companion object {
            fun initial() = UiState(
                summary = UiResult.Loading,
                queriesOverTime = UiResult.Loading,
                clientQueriesOverTime = UiResult.Loading,
            )
        }
    }

    sealed interface Effect {

    }

    sealed interface Event {
        object FetchSummary : Event
        object FetchQueriesOvertime : Event
        object FetchClientQueriesOvertime : Event
    }
}