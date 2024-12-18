package eu.wedgess.piholecontrol.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/**
 * A composable that behaves like a [Column] but adapts scrolling behavior
 * when its content exceeds the available viewport height.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param horizontalAlignment Alignment of the content horizontally within the column.
 * @param verticalArrangement Arrangement of the children vertically within the column.
 * @param contentPadding Padding around the content.
 * @param content The content inside the column.
 */
@Composable
fun ScrollAdaptiveColumn(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    var contentHeightPx by remember { mutableIntStateOf(0) }

    BoxWithConstraints(modifier = modifier) {
        val viewportHeight = maxHeight

        val contentHeightDp = with(LocalDensity.current) { contentHeightPx.toDp() }
        val scrollable = contentHeightDp > viewportHeight

        val columnModifier = Modifier
            .padding(contentPadding)
            .onGloballyPositioned { coordinates ->
                contentHeightPx = coordinates.size.height
            }
            .then(if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier)

        Column(
            modifier = columnModifier,
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement,
            content = content
        )
    }
}
