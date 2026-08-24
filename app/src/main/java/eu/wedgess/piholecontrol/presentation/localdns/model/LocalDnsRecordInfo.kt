package eu.wedgess.piholecontrol.presentation.localdns.model

data class LocalDnsRecordInfo(
    val ipAddress: String,
    val domain: String,
    val rawValue: String
)
