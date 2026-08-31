package eu.wedgess.piholecontrol.presentation.localdns.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import eu.wedgess.piholecontrol.presentation.localdns.model.LocalDnsRecordInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LocalDnsRecordItem(
    record: LocalDnsRecordInfo,
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
                vertical = PiHoleControlTheme.dimens.padding.itemContent
            ),
        verticalArrangement = Arrangement.spacedBy(
            PiHoleControlTheme.dimens.padding.itemContentXSmall
        )
    ) {
        Text(
            text = record.domain,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            text = record.ipAddress,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Normal,
                color = LocalContentColor.current.copy(
                    alpha = PiHoleControlTheme.dimens.weight.secondaryTextAlpha
                )
            )
        )
    }
}

private const val SELECTED_ITEM_ALPHA = 0.08f
