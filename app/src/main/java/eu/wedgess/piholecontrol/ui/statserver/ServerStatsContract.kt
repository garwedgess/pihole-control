package eu.wedgess.piholecontrol.ui.statserver

import eu.wedgess.piholecontrol.data.model.responses.PiHoleForwardDestinations

interface ServerStatsContract {

    data class UiState(
        val servers: PiHoleForwardDestinations
    )
}