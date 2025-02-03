package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.enums.PiHoleStatusV5
import eu.wedgess.piholecontrol.data.model.enums.PiHoleStatusV6
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleStatusResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleStatusResponseDataV6
import eu.wedgess.piholecontrol.domain.model.StatusEntity

fun StatusEntity.toPiHoleStatusResponse() = PiHoleStatusResponseDataV5(
    status = when (this) {
        StatusEntity.ENABLED -> PiHoleStatusV5.ENABLED
        StatusEntity.DISABLED -> PiHoleStatusV5.DISABLED
        StatusEntity.FAILED -> PiHoleStatusV5.UNKNOWN
        StatusEntity.UNKNOWN -> PiHoleStatusV5.UNKNOWN
    }
)

fun PiHoleStatusResponseDataV5.toStatusEntity() = when (this.status) {
    PiHoleStatusV5.ENABLED -> StatusEntity.ENABLED
    PiHoleStatusV5.DISABLED -> StatusEntity.DISABLED
    PiHoleStatusV5.UNKNOWN -> StatusEntity.UNKNOWN
}

fun PiHoleStatusResponseDataV6.toStatusEntity() = when (this.blocking) {
    PiHoleStatusV6.ENABLED -> StatusEntity.ENABLED
    PiHoleStatusV6.DISABLED -> StatusEntity.DISABLED
    PiHoleStatusV6.FAILED -> StatusEntity.FAILED
    PiHoleStatusV6.UNKNOWN -> StatusEntity.UNKNOWN
}
