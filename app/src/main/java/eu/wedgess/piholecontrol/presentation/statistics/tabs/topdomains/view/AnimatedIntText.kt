package eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.view

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    Text(text = count.formatWithThousands(), style = MaterialTheme.typography.bodySmall)
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
