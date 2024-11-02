package eu.wedgess.mihole.ui.stattopdomains

import eu.wedgess.mihole.data.model.responses.PiHoleTopQueries
import eu.wedgess.mihole.ui.stattopclients.TopClientStatsContract
import eu.wedgess.mihole.utils.UiText

interface TopDomainsStatsContract {

    data class UiState(
        val topQueries: PiHoleTopQueries
    )
}