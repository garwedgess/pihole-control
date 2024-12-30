package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleSummaryV5Data
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleOverTimeV6Data
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleSummaryV6Data
import eu.wedgess.piholecontrol.domain.model.SummaryEntity

fun PiHoleSummaryV5Data.toSummaryEntity() = SummaryEntity(
    dnsQueries = this.dnsQueriesToday,
    adsBlocked = this.adsBlockedToday,
    domainsBlocked = this.domainsBeingBlocked,
    adsPercentage = this.adsPercentageToday,
    uniqueClients = uniqueClients
)

fun PiHoleSummaryV6Data.toSummaryEntity() = SummaryEntity(
    dnsQueries = this.queries.total,
    adsBlocked = this.queries.blocked,
    domainsBlocked = this.gravity.domainsBeingBlocked,
    adsPercentage = this.queries.percentBlocked,
    uniqueClients = this.clients.total
)
