package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.enums.PiHoleStatus
import eu.wedgess.piholecontrol.data.model.responses.PiHoleStatusResponseData
import eu.wedgess.piholecontrol.domain.model.StatusEntity

fun PiHoleStatusResponseData.toStatusEntity() = when (this.blocking) {
    PiHoleStatus.ENABLED -> StatusEntity.ENABLED
    PiHoleStatus.DISABLED -> StatusEntity.DISABLED
    PiHoleStatus.FAILED -> StatusEntity.FAILED
    PiHoleStatus.UNKNOWN -> StatusEntity.UNKNOWN
}
