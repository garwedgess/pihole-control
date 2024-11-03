package eu.wedgess.piholecontrol.ui.statistics.tabs.topdomains.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import eu.wedgess.piholecontrol.ui.common.previews.ThemePreview
import eu.wedgess.piholecontrol.ui.theme.PiHoleControlTheme


@Composable
fun AnimatedLinearProgressIndicator(indicatorProgress: Float) {
    var progress by remember(indicatorProgress) {
        mutableFloatStateOf(0f)
    }
    val animatedProgress = animateFloatAsState(
        targetValue = indicatorProgress,
        visibilityThreshold = 0.001f,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "ProgressAnimation"
    ).value

    LinearProgressIndicator(
        modifier = Modifier.widthIn(max = PiHoleControlTheme.dimens.size.listPercentageBarWidth),
        strokeCap = StrokeCap.Round,
        gapSize = 0.dp,
        trackColor = MaterialTheme.colorScheme.primary.copy(
            alpha = PiHoleControlTheme.dimens.weight.minAlpha
        ),
        progress = { animatedProgress },
        drawStopIndicator = {}
    )

    LaunchedEffect(indicatorProgress) {
        progress = animatedProgress
    }

}

@ThemePreview
@Composable
private fun AnimatedLinearProgressIndicatorPreview() {
    PiHoleControlTheme {
        AnimatedLinearProgressIndicator(indicatorProgress = 0.6f)
    }
}