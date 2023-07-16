package eu.wedgess.mihole.ui.logs.view.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Http
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.PiHoleLog
import eu.wedgess.mihole.data.model.enums.LogsAnswerType
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import java.text.DateFormat

@Composable
fun LogDetailsDialog(
    filterRule: PiHoleLog,
    onConfirm: (log: PiHoleLog) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        LogDetailsDialogContent(
            log = filterRule,
            onConfirmClicked = { onConfirm(filterRule) },
            onCancelClicked = { onDismiss() }
        )
    }
}

@Composable
private fun LogDetailsDialogContent(
    log: PiHoleLog,
    onConfirmClicked: () -> Unit,
    onCancelClicked: () -> Unit
) {
    val dateTimeInstance = remember { DateFormat.getTimeInstance() }

    Surface(shape = RoundedCornerShape(MiHoleTheme.dimens.size.cornerRadius)) {
        Column(
            Modifier.padding(MiHoleTheme.dimens.padding.dialogContent),
            verticalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContent)
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                Text(stringResource(R.string.log_dialog_details_title), style = MaterialTheme.typography.titleLarge)
            }
            LogDetailsRow(
                icon = Icons.Default.Link,
                title = stringResource(R.string.log_dialog_details_label_url),
                value = log.requestedDomain
            )
            LogDetailsRow(
                icon = Icons.Default.Http,
                title = stringResource(R.string.log_dialog_details_label_type),
                value = log.queryType
            )
            LogDetailsRow(
                icon = Icons.Default.Devices,
                title = stringResource(R.string.log_dialog_details_label_device),
                value = log.client
            )
            LogDetailsRow(
                icon = Icons.Default.Schedule,
                title = stringResource(R.string.log_dialog_details_label_time),
                value = dateTimeInstance.format(log.timestamp * 1000L)
            )
            LogDetailsRow(
                icon = Icons.Default.Shield,
                title = stringResource(R.string.log_dialog_details_label_status),
                value = log.answerType.category.name
            )
            LogDetailsRow(
                icon = Icons.Default.Publish,
                title = stringResource(R.string.log_dialog_details_label_response_time),
                value = "%.1f ms".format(log.responseTime * 0.1)
            )

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancelClicked) { Text("CANCEL") }
                TextButton(onClick = onConfirmClicked) { Text("OK") }
            }

        }
    }
}

@Composable
private fun LogDetailsRow(
    icon: ImageVector,
    title: String,
    value: String,
    valueTextColor: Color = Color.Unspecified
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContent)
    ) {
        Icon(imageVector = icon, contentDescription = title)
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(text = value, style = MaterialTheme.typography.bodySmall, color = valueTextColor)
        }
    }
}

@Preview
@Composable
private fun DisplayFilterRuleDialogPreview() {
    MiHoleTheme {
        LogDetailsDialog(
            filterRule = PiHoleLog(
                answerType = LogsAnswerType.LOCAL_CACHE,
                queryType = "A",
                requestedDomain = "www.google.com",
                client = "192.168.1.1",
                responseTime = 1000,
                timestamp = 1689425287
            ),
            onConfirm = {},
            onDismiss = {}
        )
    }
}