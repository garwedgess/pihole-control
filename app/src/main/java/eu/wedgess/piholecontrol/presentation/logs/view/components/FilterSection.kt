package eu.wedgess.piholecontrol.presentation.logs.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun FilterSection(
    title: String,
    expanded: Boolean,
    onExpandedStateChanged: (Boolean) -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val animatedRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "dropdown icon animation"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow),
        verticalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContent)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            IconButton(onClick = { onExpandedStateChanged(!expanded) }) {
                Icon(
                    modifier = Modifier.graphicsLayer { rotationZ = animatedRotation },
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = ""
                )
            }
        }
        AnimatedVisibility(visible = expanded) {
            content()
        }
    }
}

@ThemePreview
@Composable
private fun FilterSectionPreview() {
    PiHoleControlTheme {
        Surface {
            FilterSection("Section 1", expanded = true, onExpandedStateChanged = {}) {
                Text("Content")
            }
        }
    }
}
