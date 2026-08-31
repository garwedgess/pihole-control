package eu.wedgess.piholecontrol.presentation.diagnosis.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.common.components.AlertMessageDialog
import eu.wedgess.piholecontrol.presentation.diagnosis.model.DiagnosisDialogType
import eu.wedgess.piholecontrol.presentation.diagnosis.model.DiagnosisMessageInfo
import eu.wedgess.piholecontrol.presentation.diagnosis.model.label
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun DiagnosisDialogs(
    dialogType: DiagnosisDialogType,
    onDismissMessageClick: (DiagnosisMessageInfo) -> Unit,
    onDismissMessageConfirmClick: () -> Unit,
    onDismissSelectedMessagesConfirmClick: () -> Unit,
    onDismissDialogClick: () -> Unit
) {
    when (dialogType) {
        is DiagnosisDialogType.ShowMessageDetails -> DiagnosisMessageDetailsDialog(
            message = dialogType.message,
            onDismissMessageClick = onDismissMessageClick,
            onDismissRequest = onDismissDialogClick
        )
        is DiagnosisDialogType.ConfirmDismissMessage -> AlertMessageDialog(
            titleText = stringResource(R.string.diagnosis_dismiss_dialog_title),
            messageText = stringResource(
                R.string.diagnosis_dismiss_dialog_message,
                dialogType.message.type
            ),
            dismissText = stringResource(R.string.all_btn_cancel),
            confirmText = stringResource(R.string.diagnosis_btn_dismiss),
            onDismiss = onDismissDialogClick,
            onConfirm = onDismissMessageConfirmClick
        )
        is DiagnosisDialogType.ConfirmDismissSelectedMessages -> AlertMessageDialog(
            titleText = stringResource(R.string.diagnosis_dismiss_selected_dialog_title),
            messageText = stringResource(
                R.string.diagnosis_dismiss_selected_dialog_message,
                dialogType.count
            ),
            dismissText = stringResource(R.string.all_btn_cancel),
            confirmText = stringResource(R.string.diagnosis_btn_dismiss),
            onDismiss = onDismissDialogClick,
            onConfirm = onDismissSelectedMessagesConfirmClick
        )
        DiagnosisDialogType.None -> Unit
    }
}

@Composable
private fun DiagnosisMessageDetailsDialog(
    message: DiagnosisMessageInfo,
    onDismissMessageClick: (DiagnosisMessageInfo) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.diagnosis_details_dialog_title)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    PiHoleControlTheme.dimens.padding.itemContent
                )
            ) {
                DiagnosisDetailText(
                    label = stringResource(R.string.diagnosis_label_type),
                    value = message.type
                )
                DiagnosisDetailText(
                    label = stringResource(R.string.diagnosis_label_severity),
                    value = message.severity.label().asString()
                )
                DiagnosisDetailText(
                    label = stringResource(R.string.diagnosis_label_message),
                    value = message.plain
                )
                message.url?.takeIf { it.isNotBlank() }?.let {
                    DiagnosisDetailText(
                        label = stringResource(R.string.diagnosis_label_url),
                        value = it
                    )
                }
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    PiHoleControlTheme.dimens.padding.itemContentXSmall
                )
            ) {
                TextButton(onClick = { onDismissMessageClick(message) }) {
                    Text(
                        text = stringResource(R.string.diagnosis_btn_dismiss),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.all_btn_close))
            }
        }
    )
}

@Composable
private fun DiagnosisDetailText(label: String, value: String) {
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
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
