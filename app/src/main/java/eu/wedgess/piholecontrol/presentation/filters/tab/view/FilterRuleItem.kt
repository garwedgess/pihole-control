package eu.wedgess.piholecontrol.presentation.filters.tab.view

import androidx.compose.foundation.background
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRuleInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilterRuleItem(
    rule: FilterRuleInfo,
    onItemClick: () -> Unit,
    onItemLongClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = SELECTED_ITEM_ALPHA)
            .compositeOver(MaterialTheme.colorScheme.background)
    } else {
        MaterialTheme.colorScheme.background
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .combinedClickable(
                onClick = onItemClick,
                onLongClick = onItemLongClick
            )
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rule.typeTitle,
                color = rule.typeColor,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                textAlign = TextAlign.End,
                text = rule.dateAdded,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Text(
            text = rule.domain,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
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
}

@ThemePreview
@Composable
private fun FilterRuleItemPreview(
    @PreviewParameter(FilterListItemPreviewParameterProvider::class) filter: FilterRuleInfo
) {
    PiHoleControlTheme {
        Surface {
            FilterRuleItem(
                rule = filter,
                isSelected = false,
                onItemClick = {},
                onItemLongClick = {}
            )
        }
    }
}

private const val SELECTED_ITEM_ALPHA = 0.08f

private class FilterListItemPreviewParameterProvider :
    PreviewParameterProvider<FilterRuleInfo> {
    override val values = sequenceOf(
        FilterRuleInfo(
            id = 0,
            dateAdded = "14-01-2022",
            dateModified = "14-01-2023",
            type = FilterRuleTypeEntity.DENY,
            domain = "www.google.com",
            enabled = true,
            comment = null,
            groups = emptyList()
        ),
        FilterRuleInfo(
            id = 1,
            dateAdded = "14-01-2022",
            dateModified = "14-01-2023",
            type = FilterRuleTypeEntity.REGEX_DENY,
            domain = "r[0-9]*----sn-.*.*.*.googlevideo.com",
            comment = "YouTube Ads",
            enabled = false,
            groups = emptyList()
        )
    )
}
