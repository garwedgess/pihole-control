package eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains

import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueries

interface TopDomainsStatsContract {

    data class UiState(
        val topQueries: PiHoleTopQueries
    )
}
