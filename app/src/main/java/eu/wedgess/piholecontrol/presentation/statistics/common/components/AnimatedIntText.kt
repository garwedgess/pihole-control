package eu.wedgess.piholecontrol.presentation.statistics.common.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.utils.extensions.formatWithThousands

@Composable
fun AnimatedIntText(value: Int) {
    val count by animateIntAsState(
        targetValue = value,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "AnimatedText"
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            PiHoleControlTheme.dimens.padding.itemContentXSmall
        )
    ) {
        Text(
            text = stringResource(R.string.statistics_label_requests),
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = PiHoleControlTheme.dimens.weight.secondaryTextAlpha
                )
            )
        )
        Text(
            text = count.formatWithThousands(),
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = PiHoleControlTheme.dimens.weight.secondaryTextAlpha
                )
            )
        )
    }
}

@ThemePreview
@Composable
private fun AnimatedIntTextPreview() {
    PiHoleControlTheme {
        Surface {
            AnimatedIntText(value = 10000)
        }
    }
}
