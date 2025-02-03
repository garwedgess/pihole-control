package eu.wedgess.piholecontrol.domain.model

data class AuthSessionStatusEntity(
    val valid: Boolean,
    val totp: Boolean,
    val sid: String,
    val validity: Int,
    val message: String
)
