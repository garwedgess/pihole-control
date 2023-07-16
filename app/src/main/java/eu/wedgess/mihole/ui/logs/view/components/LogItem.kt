package eu.wedgess.mihole.ui.logs.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import eu.wedgess.mihole.data.model.PiHoleLog
import eu.wedgess.mihole.data.model.enums.LogsAnswerCategory
import eu.wedgess.mihole.data.model.enums.LogsAnswerType
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.ui.theme.domainsOnAdListBackground
import eu.wedgess.mihole.ui.theme.queriesBlockedBackground
import eu.wedgess.mihole.ui.theme.totalQueriesBackground
import java.text.DateFormat

@Composable
fun LogItem(log: PiHoleLog, modifier: Modifier = Modifier, onItemClicked: () -> Unit) {
    val dateFormat = remember { DateFormat.getTimeInstance() }
    val (icon, tint) = when (log.answerType.category) {
        LogsAnswerCategory.BLOCK -> Pair(
            Icons.Default.GppBad,
            MaterialTheme.colorScheme.domainsOnAdListBackground
        )

        LogsAnswerCategory.ALLOW -> Pair(
            Icons.Default.GppGood,
            MaterialTheme.colorScheme.totalQueriesBackground
        )

        LogsAnswerCategory.CACHE -> Pair(
            Icons.Default.Cached,
            MaterialTheme.colorScheme.queriesBlockedBackground
        )

        LogsAnswerCategory.UNKNOWN -> Pair(
            Icons.Default.Help,
            LocalContentColor.current.copy(alpha = MiHoleTheme.dimens.weight.half)
        )
    }

    ListItem(
        modifier = modifier.clickable { onItemClicked() },
        overlineContent = {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContent)
            ) {
                Icon(
                    imageVector = icon,
                    tint = tint,
                    contentDescription = log.answerType.toString()
                )
                Text(
                    text = log.answerType.toString(), color = tint,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        headlineContent = { Text(log.requestedDomain) },
        supportingContent = { Text(log.client) },
        trailingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContent)) {
                Text(
                    modifier = Modifier.padding(top = MiHoleTheme.dimens.padding.itemContentSmall),
                    text = dateFormat.format(log.timestamp * 1000L)
                )
                Text("%.1f ms".format(log.responseTime * 0.1))
            }
        })
}

@Preview
@Composable
fun LogItemPreview() {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        LogsAnswerType.values().forEach {
            LogItem(
                log = PiHoleLog(
                    timestamp = 1616407649532,
                    queryType = "IPv4",
                    requestedDomain = "www.google.com",
                    client = "My Android",
                    answerType = it,
                    responseTime = 1200
                ),
                onItemClicked = {}
            )
        }
    }
}