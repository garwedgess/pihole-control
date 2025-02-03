package eu.wedgess.piholecontrol.presentation.statistics.tabs.topclients

import eu.wedgess.piholecontrol.domain.model.TopClientQueriesEntity

interface TopClientStatsContract {

    data class UiState(
        val topQueries: TopClientQueriesEntity
    )
}
