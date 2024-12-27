package eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains

import eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.model.TopDomainsInfo

interface TopDomainsStatsContract {

    data class UiState(
        val topPermitted: TopDomainsInfo,
        val topBlocked: TopDomainsInfo
    )
}
