package eu.wedgess.piholecontrol.presentation.localdns.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.common.components.AlertMessageDialog
import eu.wedgess.piholecontrol.presentation.localdns.model.LocalDnsDialogType
import eu.wedgess.piholecontrol.presentation.localdns.model.LocalDnsRecordDraft
import eu.wedgess.piholecontrol.presentation.localdns.model.LocalDnsRecordInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun LocalDnsDialogs(
    dialogType: LocalDnsDialogType,
    onIpAddressChange: (String) -> Unit,
    onDomainChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onEditClick: (LocalDnsRecordInfo) -> Unit,
    onDeleteClick: (LocalDnsRecordInfo) -> Unit,
    onDeleteConfirmClick: () -> Unit,
    onDeleteSelectedConfirmClick: () -> Unit,
    onDismissDialogClick: () -> Unit
) {
    when (dialogType) {
        is LocalDnsDialogType.AddLocalDnsRecord -> LocalDnsRecordFormDialog(
            title = stringResource(R.string.local_dns_add_dialog_title),
            confirmText = stringResource(R.string.local_dns_add_dialog_btn_add),
            draft = dialogType.draft,
            onIpAddressChange = onIpAddressChange,
            onDomainChange = onDomainChange,
            onConfirmClick = onAddClick,
            onDismissRequest = onDismissDialogClick
        )
        is LocalDnsDialogType.EditLocalDnsRecord -> LocalDnsRecordFormDialog(
            title = stringResource(R.string.local_dns_edit_dialog_title),
            confirmText = stringResource(R.string.local_dns_edit_dialog_btn_update),
            draft = dialogType.draft,
            onIpAddressChange = onIpAddressChange,
            onDomainChange = onDomainChange,
            onConfirmClick = onUpdateClick,
            onDismissRequest = onDismissDialogClick
        )
        is LocalDnsDialogType.ShowLocalDnsRecordInfo -> LocalDnsRecordDetailsDialog(
            record = dialogType.record,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
            onDismissRequest = onDismissDialogClick
        )
        is LocalDnsDialogType.ConfirmDeleteLocalDnsRecord -> AlertMessageDialog(
            titleText = stringResource(R.string.local_dns_delete_dialog_title),
            messageText = stringResource(
                R.string.local_dns_delete_dialog_message,
                dialogType.record.domain
            ),
            dismissText = stringResource(R.string.all_btn_cancel),
            confirmText = stringResource(R.string.local_dns_details_dialog_btn_delete),
            onDismiss = onDismissDialogClick,
            onConfirm = onDeleteConfirmClick
        )
        is LocalDnsDialogType.ConfirmDeleteSelectedLocalDnsRecords -> AlertMessageDialog(
            titleText = stringResource(R.string.local_dns_delete_selected_dialog_title),
            messageText = stringResource(
                R.string.local_dns_delete_selected_dialog_message,
                dialogType.count
            ),
            dismissText = stringResource(R.string.all_btn_cancel),
            confirmText = stringResource(R.string.local_dns_details_dialog_btn_delete),
            onDismiss = onDismissDialogClick,
            onConfirm = onDeleteSelectedConfirmClick
        )
        LocalDnsDialogType.None -> Unit
    }
}

@Composable
private fun LocalDnsRecordFormDialog(
    title: String,
    confirmText: String,
    draft: LocalDnsRecordDraft,
    onIpAddressChange: (String) -> Unit,
    onDomainChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    PiHoleControlTheme.dimens.padding.itemContent
                )
            ) {
                OutlinedTextField(
                    value = draft.ipAddress,
                    onValueChange = onIpAddressChange,
                    label = { Text(stringResource(R.string.local_dns_label_ip_address)) },
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Uri
                    )
                )
                OutlinedTextField(
                    value = draft.domain,
                    onValueChange = onDomainChange,
                    label = { Text(stringResource(R.string.local_dns_label_domain)) },
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Uri
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmClick,
                enabled = draft.canConfirm
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.all_btn_cancel))
            }
        }
    )
}

@Composable
private fun LocalDnsRecordDetailsDialog(
    record: LocalDnsRecordInfo,
    onEditClick: (LocalDnsRecordInfo) -> Unit,
    onDeleteClick: (LocalDnsRecordInfo) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.local_dns_details_dialog_title)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    PiHoleControlTheme.dimens.padding.itemContentSmall
                )
            ) {
                DetailText(
                    label = stringResource(R.string.local_dns_label_domain),
                    value = record.domain
                )
                DetailText(
                    label = stringResource(R.string.local_dns_label_ip_address),
                    value = record.ipAddress
                )
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    PiHoleControlTheme.dimens.padding.itemContentXSmall
                )
            ) {
                TextButton(onClick = { onDeleteClick(record) }) {
                    Text(
                        text = stringResource(R.string.local_dns_details_dialog_btn_delete),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                TextButton(onClick = { onEditClick(record) }) {
                    Text(stringResource(R.string.local_dns_details_dialog_btn_edit))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.all_btn_cancel))
            }
        }
    )
}

@Composable
private fun DetailText(label: String, value: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            PiHoleControlTheme.dimens.padding.itemContentXXSmall
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                color = LocalContentColor.current.copy(
                    alpha = PiHoleControlTheme.dimens.weight.secondaryTextAlpha
                )
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}
