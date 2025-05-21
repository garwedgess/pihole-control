package eu.wedgess.piholecontrol.presentation.logs.view.components.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Http
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreviewWithBackground
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun LogDetailsDialog(
    piHoleLog: LogEntryInfo,
    addToAllowList: (domain: String) -> Unit,
    addToBlockList: (domain: String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        LogDetailsDialogContent(
            log = piHoleLog,
            addToAllowList = { addToAllowList(it) },
            addToBlockList = { addToBlockList(it) },
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun LogDetailsDialogContent(
    log: LogEntryInfo,
    addToAllowList: (domain: String) -> Unit,
    addToBlockList: (domain: String) -> Unit,
    onDismiss: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            Modifier.padding(PiHoleControlTheme.dimens.padding.dialogContent),
            verticalArrangement = Arrangement.spacedBy(
                PiHoleControlTheme.dimens.padding.itemContent
            )
        ) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurface
            ) {
                Text(
                    modifier = Modifier.padding(
                        bottom = PiHoleControlTheme.dimens.padding.screenContent
                    ),
                    text = stringResource(R.string.log_dialog_details_title),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            LogDetailsRow(
                icon = Icons.Default.Link,
                title = stringResource(R.string.log_dialog_details_label_url),
                value = log.domain
            )
            LogDetailsRow(
                icon = Icons.Default.Http,
                title = stringResource(R.string.log_dialog_details_label_type),
                value = log.queryTypeString
            )
            LogDetailsRow(
                icon = Icons.Default.Devices,
                title = stringResource(R.string.log_dialog_details_label_device),
                value = log.client
            )
            LogDetailsRow(
                icon = Icons.Default.Schedule,
                title = stringResource(R.string.log_dialog_details_label_time),
                value = log.time
            )
            LogDetailsRow(
                icon = Icons.Default.Shield,
                title = stringResource(R.string.log_dialog_details_label_status),
                value = log.statusString
            )
            LogDetailsRow(
                icon = Icons.Default.Publish,
                title = stringResource(R.string.log_dialog_details_label_response_time),
                value = log.formattedReplyTime.asString()
            )

            AnimatedVisibility(visible = log.isBlocked) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { addToAllowList(log.domain) }
                ) {
                    Text(text = "Add to Allow List")
                }
            }
            AnimatedVisibility(visible = log.isAllowed) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = PiHoleControlTheme.dimens.padding.itemContent),
                    onClick = { addToBlockList(log.domain) }
                ) {
                    Text(text = "Add to Block List")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = PiHoleControlTheme.dimens.padding.dialogContent),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) { Text("Dismiss") }
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
        horizontalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.screenContent)
    ) {
        Icon(
            modifier = Modifier.size(PiHoleControlTheme.dimens.size.defaultIcon),
            imageVector = icon,
            contentDescription = title
        )
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(text = value, style = MaterialTheme.typography.bodyMedium, color = valueTextColor)
        }
    }
}

@ThemePreviewWithBackground
@Composable
private fun DisplayFilterRuleDialogPreview() {
    PiHoleControlTheme {
        LogDetailsDialog(
            piHoleLog = LogEntryInfo(
                timestamp = System.currentTimeMillis().div(1000L),
                client = "My Android",
                domain = "www.google.com.ccckjkjakjkldasjklasjkjlj",
                time = "10:12:01",
                replyTime = 1.2,
                queryType = PiHoleLogsEntity.LogEntryQueryTypeEntity.AAAA,
                id = 1,
                status = PiHoleLogsEntity.LogEntryStatusEntity.CACHE,
                dnssec = PiHoleLogsEntity.LogEntryDnssecEntity.UNKNOWN,
                replyType = PiHoleLogsEntity.LogEntryReplyTypeEntity.DOMAIN,
                listId = null,
                edeCode = -1,
                edeText = null,
                cname = null
            ),
            addToAllowList = {},
            addToBlockList = {},
            onDismiss = {}
        )
    }
}
