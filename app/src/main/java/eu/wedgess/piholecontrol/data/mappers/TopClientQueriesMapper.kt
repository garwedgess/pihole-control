package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.TopClientData
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleTopClientsResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleTopClientsCombinedResponseDataV6
import eu.wedgess.piholecontrol.domain.model.TopClientEntity
import eu.wedgess.piholecontrol.domain.model.TopClientQueriesEntity

fun PiHoleTopClientsResponseDataV5.toEntity() = TopClientQueriesEntity(
    all = this.topClientsWithPercentages.map { it.toEntity() },
    blocked = this.topClientsBlockedWithPercentages.map { it.toEntity() }
)

fun PiHoleTopClientsCombinedResponseDataV6.toEntity() = TopClientQueriesEntity(
    all = this.allWithPercentages.map { it.toEntity() },
    blocked = this.blockedWithPercentages.map { it.toEntity() }
)

fun TopClientData.toEntity() = TopClientEntity(
    client = this.client,
    hits = this.hits,
    percentage = this.percentage
)
