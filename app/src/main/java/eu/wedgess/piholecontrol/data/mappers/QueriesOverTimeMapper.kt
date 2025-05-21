package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.PiHoleOverTimeResponseData
import eu.wedgess.piholecontrol.domain.model.OverTimeEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity

fun PiHoleOverTimeResponseData.toEntity() =
    QueriesOverTimeEntity(
        permitted = this.history.map {
            OverTimeEntity(it.timestamp * 1000L, it.sumOfPermitted())
        },
        blocked = this.history.map {
            OverTimeEntity(it.timestamp * 1000L, it.sumOfBlocked())
        }
    )
