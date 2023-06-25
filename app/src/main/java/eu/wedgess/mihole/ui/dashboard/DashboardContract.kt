package eu.wedgess.mihole.ui.dashboard

import eu.wedgess.mihole.data.model.PiHoleSummary
import eu.wedgess.mihole.ui.base.Resource
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel

interface DashboardContract :
    UnidirectionalViewModel<DashboardContract.UiState, DashboardContract.Event, DashboardContract.Effect> {

    data class UiState(
        val summary: Resource<PiHoleSummary>
    ) {

        fun summary(statusSummary: Resource<PiHoleSummary>): UiState =
            this.copy(summary = statusSummary)

        companion object {
            fun initial() = UiState(
                summary = Resource.Loading
            )
        }
    }

    sealed interface Effect {

    }

    sealed interface Event {
        object FetchStatistics : Event
    }
}