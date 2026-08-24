package eu.wedgess.piholecontrol.domain.model

data class LocalDnsRecordUpdateEntity(
    val originalValue: String,
    val ipAddress: String,
    val domain: String
)
