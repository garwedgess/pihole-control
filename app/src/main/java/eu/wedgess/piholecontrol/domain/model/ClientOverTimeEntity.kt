package eu.wedgess.piholecontrol.domain.model

data class ClientOverTimeEntity(
    val clientName: String,
    val clientIp: String,
    val clientActivity: List<OverTimeEntity>
)