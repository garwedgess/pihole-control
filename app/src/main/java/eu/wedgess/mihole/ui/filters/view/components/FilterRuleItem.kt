package eu.wedgess.mihole.ui.filters.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.PiHoleFilterRules
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import eu.wedgess.mihole.ui.common.previews.ThemePreview
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.ui.theme.domainsOnAdListBackground
import eu.wedgess.mihole.ui.theme.percentageBlockedBackground
import eu.wedgess.mihole.ui.theme.queriesBlockedBackground
import eu.wedgess.mihole.ui.theme.totalQueriesBackground
import java.text.DateFormat

@Composable
fun FilterRuleItem(
    rule: PiHoleFilterRules.PiHoleFilterRule,
    onItemClicked: () -> Unit
) {
    val dateFormat = remember { DateFormat.getTimeInstance() }

    val typePair = when (rule.type) {
        FilterRuleType.WHITE -> Pair(
            stringResource(id = R.string.filters_label_allowlist),
            MaterialTheme.colorScheme.totalQueriesBackground
        )

        FilterRuleType.BLACK -> Pair(
            stringResource(id = R.string.filters_label_blocklist),
            MaterialTheme.colorScheme.domainsOnAdListBackground
        )

        FilterRuleType.REGEX_WHITE -> Pair(
            stringResource(id = R.string.filters_label_allowlist_regex),
            MaterialTheme.colorScheme.queriesBlockedBackground
        )

        FilterRuleType.REGEX_BLACK -> Pair(
            stringResource(id = R.string.filters_label_blocklist_regex),
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
                Text(
                    text = typePair.first,
                    color = typePair.second,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(text = rule.domain, maxLines = 1, overflow = TextOverflow.Ellipsis)
                rule.comment?.takeIf { it.isNotBlank() }?.run {
                    Text(
                        text = this,
                        maxLines = 1, overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal,
                            color = LocalContentColor.current.copy(alpha = MiHoleTheme.dimens.weight.secondaryTextAlpha)
                        )
                    )
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = dateFormat.format((rule.dateAdded.times(1_000L))),
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall
            )
        }

    }
}

@ThemePreview
@Composable
private fun FilterRuleItemPreview(
    @PreviewParameter(FilterListItemPreviewParameterProvider::class) filter: PiHoleFilterRules.PiHoleFilterRule
) {
    MiHoleTheme {
        Surface {
            FilterRuleItem(rule = filter, onItemClicked = {})
        }
    }
}


private class FilterListItemPreviewParameterProvider :
    PreviewParameterProvider<PiHoleFilterRules.PiHoleFilterRule> {
    override val values = sequenceOf(
        PiHoleFilterRules.PiHoleFilterRule(
            dateAdded = System.currentTimeMillis().div(1000L),
            dateModified = System.currentTimeMillis(),
            type = FilterRuleType.BLACK,
            domain = "www.google.com",
            enabled = 0
        ),
        PiHoleFilterRules.PiHoleFilterRule(
            dateAdded = System.currentTimeMillis().div(1000L),
            dateModified = System.currentTimeMillis(),
            type = FilterRuleType.REGEX_BLACK,
            domain = "r[0-9]*----sn-.*.*.*.googlevideo.com",
            comment = "YouTube Ads",
            enabled = 0
        )
    )
}