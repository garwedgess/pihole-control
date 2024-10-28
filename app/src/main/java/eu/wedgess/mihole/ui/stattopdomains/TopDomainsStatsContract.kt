package eu.wedgess.mihole.ui.stattopdomains

import eu.wedgess.mihole.data.model.responses.PiHoleTopQueries
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel
import eu.wedgess.mihole.utils.UiText

interface TopDomainsStatsContract :
    UnidirectionalViewModel<TopClientStatsContract.UiState, TopClientStatsContract.Event, TopClientStatsContract.Effect> {

    data class UiState(
        val topQueries: PiHoleTopQueries
    )

    sealed interface Effect {
        data class ShowErrorSnackbar(val msg: UiText, val retryAction: () -> Unit) : Effect
    }

    sealed interface Event {
        data object FetchDashboardInfo : Event
        data class OnRetry(val action: () -> Unit) : Event
    }
}