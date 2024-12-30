package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleOverTimeV5Data
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleOverTimeV6Data
import eu.wedgess.piholecontrol.domain.model.OverTimeEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity

fun PiHoleOverTimeV5Data.toEntity() =
    QueriesOverTimeEntity(
        permitted = this.domainsOverTime.map { (key, value) ->
            OverTimeEntity(key * 1000L, value)
        },
        blocked = this.adsOverTime.map { (key, value) ->
            OverTimeEntity(key * 1000L, value)
        }
    )

fun PiHoleOverTimeV6Data.toEntity() =
    QueriesOverTimeEntity(
        permitted = this.history.map {
            OverTimeEntity(it.timestamp * 1000L, it.sumOfPermitted())
        },
        blocked = this.history.map {
            OverTimeEntity(it.timestamp * 1000L, it.sumOfBlocked())
        }
    )
