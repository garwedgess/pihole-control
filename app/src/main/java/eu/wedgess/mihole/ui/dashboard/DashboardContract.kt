package eu.wedgess.mihole.ui.dashboard

import eu.wedgess.mihole.data.model.PiHoleClientsOverTimeData
import eu.wedgess.mihole.data.model.PiHoleOverTimeData
import eu.wedgess.mihole.data.model.PiHoleSummary
import eu.wedgess.mihole.ui.base.Resource
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel

interface DashboardContract :
    UnidirectionalViewModel<DashboardContract.UiState, DashboardContract.Event, DashboardContract.Effect> {

    data class UiState(
        val summary: Resource<PiHoleSummary>,
        val queriesOverTime: Resource<PiHoleOverTimeData>,
        val clientQueriesOverTime: Resource<PiHoleClientsOverTimeData>
    ) {

        fun summary(statusSummary: PiHoleSummary): UiState =
            this.copy(summary = Resource.Success(statusSummary))

        fun summaryError(errorMessage: String): UiState =
            this.copy(summary = Resource.Error(errorMessage))

        fun overtime(queriesOverTime: PiHoleOverTimeData): UiState =
            this.copy(queriesOverTime = Resource.Success(queriesOverTime))

        fun overtimeError(errorMessage: String): UiState =
            this.copy(queriesOverTime = Resource.Error(errorMessage))

        fun clientOvertime(queriesOverTime: PiHoleClientsOverTimeData): UiState =
            this.copy(clientQueriesOverTime = Resource.Success(queriesOverTime))

        fun clientOvertimeError(errorMessage: String): UiState =
            this.copy(clientQueriesOverTime = Resource.Error(errorMessage))

        companion object {
            fun initial() = UiState(
                summary = Resource.Loading,
                queriesOverTime = Resource.Loading,
                clientQueriesOverTime = Resource.Loading,
            )
        }
    }

    sealed interface Effect {

    }

    sealed interface Event {
        object FetchStatistics : Event
        object FetchQueriesOvertime : Event
        object FetchClientQueriesOvertime : Event
    }
}