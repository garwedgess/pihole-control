package eu.wedgess.piholecontrol.presentation.logs.view.components.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLog
import eu.wedgess.piholecontrol.data.model.enums.LogsAnswerCategory
import eu.wedgess.piholecontrol.data.model.enums.LogsAnswerType
import eu.wedgess.piholecontrol.domain.model.LogAnswerCategoryEntity
import eu.wedgess.piholecontrol.domain.model.LogAnswerTypeEntity
import eu.wedgess.piholecontrol.domain.model.LogEntryEntity
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import java.text.DateFormat

@Composable
fun LogDetailsDialog(
    piHoleLog: LogEntryEntity,
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
    log: LogEntryEntity,
    addToAllowList: (domain: String) -> Unit,
    addToBlockList: (domain: String) -> Unit,
    onDismiss: () -> Unit
) {
    val dateTimeInstance = remember { DateFormat.getTimeInstance() }

    Surface(shape = RoundedCornerShape(PiHoleControlTheme.dimens.size.cornerRadius)) {
        Column(
            Modifier.padding(PiHoleControlTheme.dimens.padding.dialogContent),
            verticalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContentLarge)
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                Text(
                    stringResource(R.string.log_dialog_details_title),
                    style = MaterialTheme.typography.titleLarge
                )
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

            AnimatedVisibility(visible = log.answerType.category == LogAnswerCategoryEntity.BLOCK) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { addToAllowList(log.requestedDomain) }) {
                    Text(text = "Add to Allow List")
                }
            }
            AnimatedVisibility(
                visible = log.answerType.category == LogAnswerCategoryEntity.ALLOW ||
                        log.answerType.category == LogAnswerCategoryEntity.CACHE
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { addToBlockList(log.requestedDomain) }) {
                    Text(text = "Add to Block List")
                }
            }

            Row(
                Modifier.fillMaxWidth(),
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

@Preview
@Composable
private fun DisplayFilterRuleDialogPreview() {
    PiHoleControlTheme {
        LogDetailsDialog(
            piHoleLog = LogEntryEntity(
                answerType = LogAnswerTypeEntity.LOCAL_CACHE,
                queryType = "A",
                requestedDomain = "www.google.com",
                client = "192.168.1.1",
                responseTime = 1000,
                timestamp = 1689425287,
                time = "10:12"
            ),
            addToAllowList = {},
            addToBlockList = {},
            onDismiss = {}
        )
    }
}