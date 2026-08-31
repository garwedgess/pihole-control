package eu.wedgess.piholecontrol.presentation.localdns.model

sealed interface LocalDnsDialogType {
    data object None : LocalDnsDialogType
    data class AddLocalDnsRecord(val draft: LocalDnsRecordDraft) : LocalDnsDialogType
    data class EditLocalDnsRecord(
        val originalValue: String,
        val draft: LocalDnsRecordDraft
    ) : LocalDnsDialogType
    data class ShowLocalDnsRecordInfo(val record: LocalDnsRecordInfo) : LocalDnsDialogType
    data class ConfirmDeleteLocalDnsRecord(val record: LocalDnsRecordInfo) : LocalDnsDialogType
    data class ConfirmDeleteSelectedLocalDnsRecords(val count: Int) : LocalDnsDialogType
}
