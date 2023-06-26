package eu.wedgess.mihole.utils.vico

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.textComponent
import eu.wedgess.mihole.ui.theme.domainsOnAdListBackground
import eu.wedgess.mihole.ui.theme.totalQueriesBackground

@Composable
fun rememberLegend() = horizontalLegend(
    items = listOf(
        LegendItem(
            icon = shapeComponent(
                shape = Shapes.pillShape,
                color = MaterialTheme.colorScheme.totalQueriesBackground
            ),
            label = textComponent(),
            labelText = "Queries"
        ),
        LegendItem(
            icon = shapeComponent(
                shape = Shapes.pillShape,
                color = MaterialTheme.colorScheme.domainsOnAdListBackground
            ),
            label = textComponent(),
            labelText = "Blocked"
        )
    ),
    iconSize = 18.dp,
    iconPadding = 8.dp,
    spacing = 120.dp
)