package eu.wedgess.piholecontrol.presentation.logs.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
                vertical = PiHoleControlTheme.dimens.padding.screenContent
            ),
        verticalArrangement = Arrangement.spacedBy(
            PiHoleControlTheme.dimens.padding.itemContentSmall
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(PiHoleControlTheme.dimens.weight.point8)
                    .padding(end = PiHoleControlTheme.dimens.padding.itemContent)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Text(text = log.domain, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(
                    text = log.client,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal,
                        color = LocalContentColor.current.copy(
                            alpha = PiHoleControlTheme.dimens.weight.secondaryTextAlpha
                        )
                    )
                )
            }
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = PiHoleControlTheme.dimens.padding.itemContentSmall),
                    textAlign = TextAlign.End,
                    text = log.time,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = PiHoleControlTheme.dimens.padding.itemContent),
                    textAlign = TextAlign.End,
                    text = log.formattedReplyTime.asString(),
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@ThemePreview
@Composable
private fun LogListItemPreview() {
    PiHoleControlTheme {
        Surface {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                PiHoleLogsEntity.LogsAnswerTypeEntity.entries.forEach {
                    LogListItem(
                        log = LogEntryInfo.Version5(
                            timestamp = System.currentTimeMillis().div(1000L),
                            time = "10:12:01",
                            queryType = "IPv4",
                            domain = "www.google.com",
                            client = "My Android",
                            answerType = it,
                            replyTime = 1.2
                        ),
                        onItemClick = {}
                    )
                }
            }
        }
    }
}
