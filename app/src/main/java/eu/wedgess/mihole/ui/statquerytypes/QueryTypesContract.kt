package eu.wedgess.mihole.ui.statquerytypes

import eu.wedgess.mihole.data.model.responses.QueryTypes
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel
import eu.wedgess.mihole.ui.stattopdomains.TopClientStatsContract
import eu.wedgess.mihole.utils.UiText

interface QueryTypesContract :
    UnidirectionalViewModel<TopClientStatsContract.UiState, TopClientStatsContract.Event, TopClientStatsContract.Effect> {

    data class UiState(
        val queryTypes: QueryTypes
    )

    sealed interface Effect {
        data class ShowErrorSnackbar(val msg: UiText, val retryAction: () -> Unit) : Effect
    }

    sealed interface Event {
        data object FetchDashboardInfo : Event
        data class OnRetry(val action: () -> Unit) : Event
    }
}