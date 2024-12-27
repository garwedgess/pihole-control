package eu.wedgess.piholecontrol.presentation.statistics.tabs.topclients.model

import eu.wedgess.piholecontrol.domain.model.TopClientEntity

data class TopClientsInfo(
    val topClients: List<TopClientEntity>
) {
    val allHits: Int get() = topClients.sumOf { it.hits }
}
