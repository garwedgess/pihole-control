package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.PiHoleSummary
import eu.wedgess.piholecontrol.domain.model.SummaryEntity

fun PiHoleSummary.toSummaryEntity() = SummaryEntity(
    dnsQueries = this.dnsQueriesToday,
    adsBlocked = this.adsBlockedToday,
    domainsBlocked = this.domainsBeingBlocked,
    adsPercentage = this.adsPercentageToday,
    uniqueClients = uniqueClients
)
