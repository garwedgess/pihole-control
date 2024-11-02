package eu.wedgess.mihole.ui.statserver

import eu.wedgess.mihole.data.model.responses.PiHoleForwardDestinations
import eu.wedgess.mihole.utils.UiText

interface ServerStatsContract {

    data class UiState(
        val servers: PiHoleForwardDestinations
    )
}