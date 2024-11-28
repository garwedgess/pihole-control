package eu.wedgess.piholecontrol.domain.mappers

import eu.wedgess.piholecontrol.data.model.responses.PiHoleOverTimeData
import eu.wedgess.piholecontrol.domain.model.OverTimeEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity

fun PiHoleOverTimeData.toQueriesOverTimeData() =
    QueriesOverTimeEntity(
        permitted = this.domainsOverTime.map { (key, value) ->
            OverTimeEntity(key * 1000L, value)
        },
        blocked = this.adsOverTime.map { (key, value) ->
            OverTimeEntity(key * 1000L, value)
        }
    )