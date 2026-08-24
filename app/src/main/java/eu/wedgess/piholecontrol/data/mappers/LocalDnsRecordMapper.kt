package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.domain.model.LocalDnsRecordEntity

fun String.toLocalDnsRecordEntity(): LocalDnsRecordEntity? {
    val parts = trim().split(LOCAL_DNS_HOST_WHITESPACE_REGEX)
    if (parts.size < 2) return null

    return LocalDnsRecordEntity(
        ipAddress = parts.first(),
        domain = parts.drop(1).joinToString(" "),
        rawValue = this
    )
}

fun formatLocalDnsRecordValue(ipAddress: String, domain: String): String {
    return "${ipAddress.trim()} ${domain.trim()}"
}

private val LOCAL_DNS_HOST_WHITESPACE_REGEX = Regex("\\s+")
