package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.PiHoleSummaryResponseData
import eu.wedgess.piholecontrol.domain.model.SummaryEntity

fun PiHoleSummaryResponseData.toEntity() = SummaryEntity(
    dnsQueries = this.queries.total,
    adsBlocked = this.queries.blocked,
    domainsBlocked = this.gravity.domainsBeingBlocked,
    adsPercentage = this.queries.percentBlocked,
    uniqueClients = this.clients.total
)
