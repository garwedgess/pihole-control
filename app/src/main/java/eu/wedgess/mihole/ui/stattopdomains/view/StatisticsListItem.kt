package eu.wedgess.mihole.ui.stattopdomains.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import eu.wedgess.mihole.ui.common.previews.ThemePreview
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import kotlin.math.roundToInt

@Composable
fun StatisticsListItem(
    domain: String,
    hits: Int,
    progress: Float
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
                .weight(MiHoleTheme.dimens.weight.point8)
                .padding(end = MiHoleTheme.dimens.padding.screenContent),
            verticalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContentSmall)
        ) {
            Text(
                text = domain,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AnimatedIntText(value = hits)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${progress.times(100).roundToInt()} %",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AnimatedLinearProgressIndicator(indicatorProgress = progress)
        }
    }
}

@ThemePreview
@Composable
private fun StatisticsListItemPreview() {
    MiHoleTheme {
        Surface {
            StatisticsListItem(
                domain = "www.google.com",
                hits = 4234,
                progress = 0.2f
            )
        }
    }
}