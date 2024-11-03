package eu.wedgess.piholecontrol.ui.statistics.tabs.server

import eu.wedgess.piholecontrol.data.model.responses.PiHoleForwardDestinations

interface ServerStatsContract {

    data class UiState(
        val servers: PiHoleForwardDestinations
    )
}