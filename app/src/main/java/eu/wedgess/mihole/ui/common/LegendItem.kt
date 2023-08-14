package eu.wedgess.mihole.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import eu.wedgess.mihole.ui.common.model.LegendData
import eu.wedgess.mihole.ui.common.previews.ThemePreview
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun RowScope.LegendItem(legendData: LegendData) {
    val defaultTitleText = MaterialTheme.typography.bodySmall.copy(
        fontWeight = FontWeight.Bold,
        fontSize = MiHoleTheme.dimens.fontSize.legendTitle,
        color = MaterialTheme.colorScheme.onSurface
    )

    val selectedTitleText = MaterialTheme.typography.bodySmall.copy(
        fontWeight = FontWeight.Bold,
        fontSize = MiHoleTheme.dimens.fontSize.legendTitleSelected,
        color = legendData.color
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {
        Box(
            modifier = Modifier
                .size(MiHoleTheme.dimens.size.legendIcon)
                .clip(CircleShape)
                .background(legendData.color)
        )
        Column(modifier = Modifier.padding(start = MiHoleTheme.dimens.padding.itemContent)) {
            Text(
                text = legendData.title,
                style = if (legendData.isSelected) selectedTitleText else defaultTitleText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            legendData.subTitle?.run {
                Text(
                    text = this,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = MiHoleTheme.dimens.fontSize.legendSubTitle,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = MiHoleTheme.dimens.weight.secondaryTextAlpha)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@ThemePreview
@Composable
private fun LegendItemPreview() {
    Row {

        LegendItem(
            LegendData(
                title = "Test title",
                subTitle = "Test subtitle",
                color = Color.Blue
            )
        )
    }
}