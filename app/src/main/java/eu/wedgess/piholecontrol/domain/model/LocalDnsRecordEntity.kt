package eu.wedgess.piholecontrol.domain.model

data class LocalDnsRecordEntity(
    val ipAddress: String,
    val domain: String,
    val rawValue: String
)
