package eu.wedgess.mihole.ui.stattopclients

import eu.wedgess.mihole.data.model.responses.PiHoleTopQueries
import eu.wedgess.mihole.utils.UiText

interface TopClientStatsContract {

    data class UiState(
        val topQueries: PiHoleTopQueries
    )
}