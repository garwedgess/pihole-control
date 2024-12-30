package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.enums.PiHoleStatus
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleStatusResponse
import eu.wedgess.piholecontrol.domain.model.StatusEntity

fun StatusEntity.toPiHoleStatusResponse() = PiHoleStatusResponse(
    status = when (this) {
        StatusEntity.ENABLED -> PiHoleStatus.ENABLED
        StatusEntity.DISABLED -> PiHoleStatus.DISABLED
        StatusEntity.UNKNOWN -> PiHoleStatus.UNKNOWN
    }
)

fun PiHoleStatusResponse.toStatusEntity() = when (this.status) {
    PiHoleStatus.ENABLED -> StatusEntity.ENABLED
    PiHoleStatus.DISABLED -> StatusEntity.DISABLED
    PiHoleStatus.UNKNOWN -> StatusEntity.UNKNOWN
}
