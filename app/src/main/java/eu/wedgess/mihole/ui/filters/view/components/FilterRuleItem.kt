package eu.wedgess.mihole.ui.filters.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.PiHoleFilterRules
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import eu.wedgess.mihole.data.model.enums.isRegex
import eu.wedgess.mihole.ui.common.previews.ThemePreview
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import java.text.DateFormat

@Composable
fun FilterRuleItem(
    rule: PiHoleFilterRules.PiHoleFilterRule,
    dateFormat: DateFormat,
    onItemClicked: () -> Unit
) {
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
            verticalAlignment = if (rule.type.isRegex()) Alignment.Top else Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(end = MiHoleTheme.dimens.padding.itemContent)
            ) {
                if (rule.type.isRegex()) {
                    Text(
                        text = stringResource(id = R.string.filters_label_regex),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Text(text = rule.domain)
                rule.comment?.takeIf { it.isNotBlank() }?.run {
                    Text(text = this, style = MaterialTheme.typography.labelLarge)
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
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
        val dateTimeInstance = remember { DateFormat.getDateInstance() }
        FilterRuleItem(rule = filter, dateFormat = dateTimeInstance, onItemClicked = {})
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