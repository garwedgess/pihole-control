package eu.wedgess.piholecontrol.data.model.responses.v6

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleAuthSessionStatusResponseDataV6(
    @SerialName("session") var session: PiHoleAuthSession = PiHoleAuthSession(),
    @SerialName("took") var took: Double? = null
) {
    @Serializable
    data class PiHoleAuthSession(
        @SerialName("valid") var valid: Boolean = false,
        @SerialName("totp") var totp: Boolean = false,
        @SerialName("sid") var sid: String? = null,
        @SerialName("csrf") var csrf: String? = null,
        @SerialName("validity") var validity: Int = -1,
        @SerialName("message") var message: String = ""
    )
}
