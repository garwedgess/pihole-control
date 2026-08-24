package eu.wedgess.piholecontrol.presentation.localdns.model

data class LocalDnsRecordDraft(
    val ipAddress: String = "",
    val domain: String = ""
) {
    val canConfirm: Boolean
        get() = ipAddress.isNotBlank() && domain.isNotBlank()
}
