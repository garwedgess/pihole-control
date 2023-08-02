package eu.wedgess.mihole.ui.logs.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.PiHoleLog
import eu.wedgess.mihole.data.model.enums.LogsAnswerType
import eu.wedgess.mihole.ui.common.previews.ThemePreview
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.ui.theme.domainsOnAdListBackground
import eu.wedgess.mihole.ui.theme.percentageBlockedBackground
import eu.wedgess.mihole.ui.theme.queriesBlockedBackground
import eu.wedgess.mihole.ui.theme.totalQueriesBackground
import java.text.DateFormat

@Composable
fun LogListItem(log: PiHoleLog, onItemClicked: () -> Unit) {
    val dateFormat = remember { DateFormat.getTimeInstance() }

    val typePair = when (log.answerType) {
        LogsAnswerType.UPSTREAM -> Triple(
            stringResource(id = R.string.logs_type_label_allow_upstream),
            Icons.Default.GppGood,
            MaterialTheme.colorScheme.totalQueriesBackground
        )

        LogsAnswerType.ALREADY_FORWARDED -> Triple(
            stringResource(id = R.string.logs_type_label_allow_already_forwarded),
            Icons.Default.GppGood,
            MaterialTheme.colorScheme.totalQueriesBackground
        )

        LogsAnswerType.LOCAL_CACHE -> Triple(
            stringResource(id = R.string.logs_type_label_allow_cache),
            Icons.Default.Cached,
            MaterialTheme.colorScheme.queriesBlockedBackground
        )

        LogsAnswerType.RETRIED -> Triple(
            stringResource(id = R.string.logs_type_label_allow_retried),
            Icons.Default.GppGood,
            MaterialTheme.colorScheme.totalQueriesBackground
        )

        LogsAnswerType.RETRIED_IGNORED -> Triple(
            stringResource(id = R.string.logs_type_label_allow_retried_ignored),
            Icons.Default.GppGood,
            MaterialTheme.colorScheme.totalQueriesBackground
        )

        LogsAnswerType.GRAVITY_BLOCK -> Triple(
            stringResource(id = R.string.logs_type_label_block_gravity),
            Icons.Default.GppBad,
            MaterialTheme.colorScheme.domainsOnAdListBackground
        )

        LogsAnswerType.REGEX_BLOCK -> Triple(
            stringResource(id = R.string.logs_type_label_block_gravity),
            Icons.Default.GppBad,
            MaterialTheme.colorScheme.domainsOnAdListBackground
        )

        LogsAnswerType.EXACT_BLOCK -> Triple(
            stringResource(id = R.string.logs_type_label_block_exact),
            Icons.Default.GppBad,
            MaterialTheme.colorScheme.domainsOnAdListBackground
        )

        LogsAnswerType.EXTERNAL_IP_BLOCK -> Triple(
            stringResource(id = R.string.logs_type_label_block_external_ip),
            Icons.Default.GppBad,
            MaterialTheme.colorScheme.domainsOnAdListBackground
        )

        LogsAnswerType.EXTERNAL_NULL_BLOCK -> Triple(
            stringResource(id = R.string.logs_type_label_block_external_null),
            Icons.Default.GppBad,
            MaterialTheme.colorScheme.domainsOnAdListBackground
        )

        LogsAnswerType.EXTERNAL_NXRA_BLOCK -> Triple(
            stringResource(id = R.string.logs_type_label_block_external_nrxa),
            Icons.Default.GppBad,
            MaterialTheme.colorScheme.domainsOnAdListBackground
        )

        LogsAnswerType.CNAME_GRAVITY_BLOCK -> Triple(
            stringResource(id = R.string.logs_type_label_block_gravity_cname),
            Icons.Default.GppBad,
            MaterialTheme.colorScheme.domainsOnAdListBackground
        )

        LogsAnswerType.CNAME_REGEX_BLOCK -> Triple(
            stringResource(id = R.string.logs_type_label_block_regex_cname),
            Icons.Default.GppBad,
            MaterialTheme.colorScheme.domainsOnAdListBackground
        )

        LogsAnswerType.CNAME_EXACT_BLOCK -> Triple(
            stringResource(id = R.string.logs_type_label_block_exact_cname),
            Icons.Default.GppBad,
            MaterialTheme.colorScheme.domainsOnAdListBackground
        )

        LogsAnswerType.UNKNOWN -> Triple(
            stringResource(id = R.string.logs_type_label_unknown),
            Icons.Default.Help,
            MaterialTheme.colorScheme.percentageBlockedBackground
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked() }
            .padding(
                horizontal = MiHoleTheme.dimens.padding.screenContent,
                vertical = MiHoleTheme.dimens.padding.screenContent
            ),
        verticalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContentSmall)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(end = MiHoleTheme.dimens.padding.itemContent)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContent)
                ) {
                    Icon(
                        imageVector = typePair.second,
                        contentDescription = typePair.first,
                        tint = typePair.third
                    )
                    Text(
                        text = typePair.first,
                        color = typePair.third,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Text(text = log.requestedDomain, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(
                    text = log.client,
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal,
                        color = LocalContentColor.current.copy(alpha = MiHoleTheme.dimens.weight.secondaryTextAlpha)
                    )
                )
            }
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MiHoleTheme.dimens.padding.itemContentSmall),
                    textAlign = TextAlign.End,
                    text = dateFormat.format((log.timestamp.times(1_000L))),
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MiHoleTheme.dimens.padding.itemContent),
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
    MiHoleTheme {
        Surface {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                LogsAnswerType.values().forEach {
                    LogListItem(log = PiHoleLog(
                        timestamp = System.currentTimeMillis().div(1000L),
                        queryType = "IPv4",
                        requestedDomain = "www.google.com",
                        client = "My Android",
                        answerType = it,
                        responseTime = 1200
                    ), onItemClicked = {})
                }
            }
        }
    }
}