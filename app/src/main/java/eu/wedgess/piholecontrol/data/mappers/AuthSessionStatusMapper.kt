package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.PiHoleAuthSessionStatusResponseData
import eu.wedgess.piholecontrol.domain.model.AuthSessionStatusEntity

fun PiHoleAuthSessionStatusResponseData.toEntity() = AuthSessionStatusEntity(
    valid = this.session.valid,
    totp = this.session.totp,
    sid = this.session.sid ?: "",
    validity = this.session.validity,
    message = this.session.message
)
