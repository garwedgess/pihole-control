package eu.wedgess.piholecontrol.presentation.filters.tab.view

import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRuleInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun FilterRuleItem(
    rule: FilterRuleInfo,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clickable { onItemClick() }
            .padding(
                horizontal = PiHoleControlTheme.dimens.padding.screenContent,
                vertical = PiHoleControlTheme.dimens.padding.itemContent
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(end = PiHoleControlTheme.dimens.padding.itemContent),
            verticalArrangement = Arrangement.spacedBy(
                PiHoleControlTheme.dimens.padding.itemContentXSmall
            )
        ) {
            Text(
                text = rule.typeTitle,
                color = rule.typeColor,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = rule.domain,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )
            rule.comment?.takeIf { it.isNotBlank() }?.run {
                Text(
                    text = this@run,
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
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            text = rule.dateAdded,
            maxLines = 1,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@ThemePreview
@Composable
private fun FilterRuleItemPreview(
    @PreviewParameter(FilterListItemPreviewParameterProvider::class) filter: FilterRuleInfo
) {
    PiHoleControlTheme {
        Surface {
            FilterRuleItem(rule = filter, onItemClick = {})
        }
    }
}

private class FilterListItemPreviewParameterProvider :
    PreviewParameterProvider<FilterRuleInfo> {
    override val values = sequenceOf(
        FilterRuleInfo(
            id = 0,
            dateAdded = "14-01-2022",
            dateModified = "14-01-2023",
            type = FilterRuleTypeEntity.BLOCK,
            domain = "www.google.com",
            enabled = true,
            comment = null,
            groups = emptyList()
        ),
        FilterRuleInfo(
            id = 1,
            dateAdded = "14-01-2022",
            dateModified = "14-01-2023",
            type = FilterRuleTypeEntity.REGEX_BLOCK,
            domain = "r[0-9]*----sn-.*.*.*.googlevideo.com",
            comment = "YouTube Ads",
            enabled = false,
            groups = emptyList()
        )
    )
}
