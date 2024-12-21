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
import eu.wedgess.piholecontrol.domain.model.LogAnswerTypeEntity
import eu.wedgess.piholecontrol.domain.model.LogEntryEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.logs.extensions.toColor
import eu.wedgess.piholecontrol.presentation.logs.extensions.toIcon
import eu.wedgess.piholecontrol.presentation.logs.extensions.toStringValue
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun LogListItem(log: LogEntryEntity, onItemClick: () -> Unit) {
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
                        imageVector = log.answerType.toIcon(),
                        contentDescription = log.answerType.name,
                        tint = log.answerType.toColor()
                    )
                    Text(
                        text = log.answerType.toStringValue(),
                        color = log.answerType.toColor(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Text(text = log.requestedDomain, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
                    text = "%.1f ms".format(log.responseTime * 0.1),
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
                LogAnswerTypeEntity.entries.forEach {
                    LogListItem(
                        log = LogEntryEntity(
                            timestamp = System.currentTimeMillis().div(1000L),
                            time = "10:12:01",
                            queryType = "IPv4",
                            requestedDomain = "www.google.com",
                            client = "My Android",
                            answerType = it,
                            responseTime = 1200
                        ),
                        onItemClick = {}
                    )
                }
            }
        }
    }
}
