package eu.wedgess.piholecontrol.presentation.logs.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun LogListItem(log: LogEntryInfo, onItemClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(
                horizontal = PiHoleControlTheme.dimens.padding.screenContent,
                vertical = PiHoleControlTheme.dimens.padding.itemContentSmall
            ),
        verticalArrangement = Arrangement.spacedBy(
            PiHoleControlTheme.dimens.padding.itemContentXXSmall
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(PiHoleControlTheme.dimens.weight.point8),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    PiHoleControlTheme.dimens.padding.itemContent
                )
            ) {
                Icon(
                    imageVector = log.icon(),
                    contentDescription = log.stringValue(),
                    tint = log.color()
                )
                Text(
                    text = log.stringValue(),
                    color = log.color(),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Text(
                modifier = Modifier.weight(PiHoleControlTheme.dimens.weight.point2),
                textAlign = TextAlign.End,
                text = log.time,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(PiHoleControlTheme.dimens.weight.point8),
                    text = log.domain,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.weight(PiHoleControlTheme.dimens.weight.point2),
                    textAlign = TextAlign.End,
                    text = log.formattedReplyTime.asString(),
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Text(
                text = log.client,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Normal,
                    color = LocalContentColor.current.copy(
                        alpha = PiHoleControlTheme.dimens.weight.secondaryTextAlpha
                    )
                )
            )
        }
    }
}

@ThemePreview
@Composable
private fun LogListItemPreview(
    @PreviewParameter(LogListItemPreviewProvider::class) logItem: LogEntryInfo
) {
    PiHoleControlTheme {
        Surface {
            LogListItem(
                log = logItem,
                onItemClick = {}
            )
        }
    }
}

private class LogListItemPreviewProvider : PreviewParameterProvider<LogEntryInfo> {
    override val values: Sequence<LogEntryInfo>
        get() = PiHoleLogsEntity.LogEntryStatusEntity.entries.asSequence().map {
            LogEntryInfo(
                timestamp = System.currentTimeMillis().div(1000L),
                client = "My Android",
                domain = "www.google.com.ccckjkjakjkldasjklasjkjlj",
                time = "10:12:01",
                replyTime = 1.2,
                queryType = PiHoleLogsEntity.LogEntryQueryTypeEntity.AAAA,
                id = 1,
                status = it,
                dnssec = PiHoleLogsEntity.LogEntryDnssecEntity.UNKNOWN,
                replyType = PiHoleLogsEntity.LogEntryReplyTypeEntity.DOMAIN,
                listId = null,
                edeCode = -1,
                edeText = null,
                cname = null,
            )
        }
}
