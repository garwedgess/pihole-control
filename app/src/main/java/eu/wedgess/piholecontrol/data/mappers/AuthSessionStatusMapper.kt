package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleAuthSessionStatusResponseDataV6
import eu.wedgess.piholecontrol.domain.model.AuthSessionStatusEntity

fun PiHoleAuthSessionStatusResponseDataV6.toEntity() = AuthSessionStatusEntity(
    valid = this.session.valid,
    totp = this.session.totp,
    sid = this.session.sid ?: "",
    validity = this.session.validity,
    message = this.session.message
)
