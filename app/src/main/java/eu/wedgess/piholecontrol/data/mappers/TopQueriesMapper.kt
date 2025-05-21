package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.TopQueryData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueriesCombinedResponseData
import eu.wedgess.piholecontrol.domain.model.TopDomainEntity
import eu.wedgess.piholecontrol.domain.model.TopQueriesEntity

fun PiHoleTopQueriesCombinedResponseData.toEntity() = TopQueriesEntity(
    allowed = this.permittedWithPercentages.map { it.toEntity() },
    blocked = this.blockedWithPercentages.map { it.toEntity() }
)

fun TopQueryData.toEntity() = TopDomainEntity(
    domain = this.domain,
    hits = this.hits,
    percentage = this.percentage
)
