package eu.wedgess.piholecontrol.ui.stattopclients

import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueries

interface TopClientStatsContract {

    data class UiState(
        val topQueries: PiHoleTopQueries
    )
}