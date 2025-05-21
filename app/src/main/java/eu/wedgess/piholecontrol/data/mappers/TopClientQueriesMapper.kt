package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.TopClientData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopClientsCombinedResponseData
import eu.wedgess.piholecontrol.domain.model.TopClientEntity
import eu.wedgess.piholecontrol.domain.model.TopClientQueriesEntity

fun PiHoleTopClientsCombinedResponseData.toEntity() = TopClientQueriesEntity(
    all = this.allWithPercentages.map { it.toEntity() },
    blocked = this.blockedWithPercentages.map { it.toEntity() }
)

fun TopClientData.toEntity() = TopClientEntity(
    client = this.client,
    hits = this.hits,
    percentage = this.percentage
)
