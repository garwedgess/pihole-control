package eu.wedgess.piholecontrol.presentation.localdns.extensions

import eu.wedgess.piholecontrol.domain.model.LocalDnsRecordEntity
import eu.wedgess.piholecontrol.domain.model.LocalDnsRecordUpdateEntity
import eu.wedgess.piholecontrol.presentation.localdns.model.LocalDnsRecordDraft
import eu.wedgess.piholecontrol.presentation.localdns.model.LocalDnsRecordInfo

fun LocalDnsRecordEntity.toInfo() = LocalDnsRecordInfo(
    ipAddress = ipAddress,
    domain = domain,
    rawValue = rawValue
)

fun LocalDnsRecordInfo.toDraft() = LocalDnsRecordDraft(
    ipAddress = ipAddress,
    domain = domain
)

fun LocalDnsRecordDraft.toInfo(): LocalDnsRecordInfo {
    val ipAddress = ipAddress.trim()
    val domain = domain.trim()
    return LocalDnsRecordInfo(
        ipAddress = ipAddress,
        domain = domain,
        rawValue = "$ipAddress $domain"
    )
}

fun LocalDnsRecordDraft.toUpdateEntity(originalValue: String) = LocalDnsRecordUpdateEntity(
    originalValue = originalValue,
    ipAddress = ipAddress,
    domain = domain
)
