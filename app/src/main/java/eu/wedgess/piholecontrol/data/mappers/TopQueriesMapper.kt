package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.TopQueryData
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleTopQueriesResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleTopQueriesCombinedResponseV6Data
import eu.wedgess.piholecontrol.domain.model.TopDomainEntity
import eu.wedgess.piholecontrol.domain.model.TopQueriesEntity

fun PiHoleTopQueriesResponseDataV5.toEntity() = TopQueriesEntity(
    allowed = this.topQueriesPercentages.map { it.toEntity() },
    blocked = this.topAdsPercentages.map { it.toEntity() }
)

fun PiHoleTopQueriesCombinedResponseV6Data.toEntity() = TopQueriesEntity(
    allowed = this.permittedWithPercentages.map { it.toEntity() },
    blocked = this.blockedWithPercentages.map { it.toEntity() }
)

fun TopQueryData.toEntity() = TopDomainEntity(
    domain = this.domain,
    hits = this.hits,
    percentage = this.percentage
)
