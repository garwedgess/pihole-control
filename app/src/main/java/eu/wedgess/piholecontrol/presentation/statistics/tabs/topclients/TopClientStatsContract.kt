package eu.wedgess.piholecontrol.presentation.statistics.tabs.topclients

import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueries

interface TopClientStatsContract {

    data class UiState(
        val topQueries: PiHoleTopQueries
    )
}