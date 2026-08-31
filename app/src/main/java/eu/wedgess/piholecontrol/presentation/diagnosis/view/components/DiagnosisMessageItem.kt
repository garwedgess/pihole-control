package eu.wedgess.piholecontrol.presentation.diagnosis.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import eu.wedgess.piholecontrol.presentation.diagnosis.model.DiagnosisMessageInfo
import eu.wedgess.piholecontrol.presentation.diagnosis.model.color
import eu.wedgess.piholecontrol.presentation.diagnosis.model.icon
import eu.wedgess.piholecontrol.presentation.diagnosis.model.label
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiagnosisMessageItem(
    message: DiagnosisMessageInfo,
    isSelected: Boolean,
    showDismissAction: Boolean,
    onItemClick: () -> Unit,
    onItemLongClick: () -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = SELECTED_ITEM_ALPHA)
            .compositeOver(MaterialTheme.colorScheme.surfaceContainerLow)
    } else {
        MaterialTheme.colorScheme.surfaceContainerLow
    }
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = PiHoleControlTheme.dimens.padding.screenContent,
                vertical = PiHoleControlTheme.dimens.padding.itemContentXSmall
            ),
        shape = MaterialTheme.shapes.medium,
        color = containerColor,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .combinedClickable(
                    onClick = onItemClick,
                    onLongClick = onItemLongClick
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(message.severity.color())
            )
            Column(
                modifier = Modifier
                    .weight(PiHoleControlTheme.dimens.weight.full)
                    .padding(PiHoleControlTheme.dimens.padding.itemContent),
                verticalArrangement = Arrangement.spacedBy(
                    PiHoleControlTheme.dimens.padding.itemContentXSmall
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        PiHoleControlTheme.dimens.padding.itemContentSmall
                    )
                ) {
                    Icon(
                        imageVector = message.severity.icon(),
                        contentDescription = null,
                        tint = message.severity.color()
                    )
                    Text(
                        text = message.severity.label().asString(),
                        color = message.severity.color(),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = message.type,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = LocalContentColor.current.copy(
                                alpha = PiHoleControlTheme.dimens.weight.secondaryTextAlpha
                            )
                        )
                    )
                }
                Text(
                    text = message.plain,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = message.formattedTimestamp(),
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = LocalContentColor.current.copy(
                            alpha = PiHoleControlTheme.dimens.weight.tertiaryTextAlpha
                        )
                    )
                )
            }
            if (showDismissAction) {
                IconButton(onClick = onDismissClick) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private const val SELECTED_ITEM_ALPHA = 0.08f

private fun DiagnosisMessageInfo.formattedTimestamp(): String {
    return DateFormat.getDateTimeInstance(
        DateFormat.MEDIUM,
        DateFormat.SHORT
    ).format(Date(timestamp * 1_000))
}
