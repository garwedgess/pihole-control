package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleOverTimeResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleOverTimeResponseDataV6
import eu.wedgess.piholecontrol.domain.model.OverTimeEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity

fun PiHoleOverTimeResponseDataV5.toEntity() =
    QueriesOverTimeEntity(
        permitted = this.domainsOverTime.map { (key, value) ->
            OverTimeEntity(key * 1000L, value)
        },
        blocked = this.adsOverTime.map { (key, value) ->
            OverTimeEntity(key * 1000L, value)
        }
    )

fun PiHoleOverTimeResponseDataV6.toEntity() =
    QueriesOverTimeEntity(
        permitted = this.history.map {
            OverTimeEntity(it.timestamp * 1000L, it.sumOfPermitted())
        },
        blocked = this.history.map {
            OverTimeEntity(it.timestamp * 1000L, it.sumOfBlocked())
        }
    )
