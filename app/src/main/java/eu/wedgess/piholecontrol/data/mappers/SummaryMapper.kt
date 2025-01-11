package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleSummaryResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleSummaryResponseDataV6
import eu.wedgess.piholecontrol.domain.model.SummaryEntity

fun PiHoleSummaryResponseDataV5.toSummaryEntity() = SummaryEntity(
    dnsQueries = this.dnsQueriesToday,
    adsBlocked = this.adsBlockedToday,
    domainsBlocked = this.domainsBeingBlocked,
    adsPercentage = this.adsPercentageToday,
    uniqueClients = uniqueClients
)

fun PiHoleSummaryResponseDataV6.toSummaryEntity() = SummaryEntity(
    dnsQueries = this.queries.total,
    adsBlocked = this.queries.blocked,
    domainsBlocked = this.gravity.domainsBeingBlocked,
    adsPercentage = this.queries.percentBlocked,
    uniqueClients = this.clients.total
)
