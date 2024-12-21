package eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.model

import eu.wedgess.piholecontrol.domain.model.TopDomainEntity

data class TopDomainsInfo(
    val topDomains: List<TopDomainEntity>
) {
    val allHits: Int get() = topDomains.sumOf { it.hits }
}
