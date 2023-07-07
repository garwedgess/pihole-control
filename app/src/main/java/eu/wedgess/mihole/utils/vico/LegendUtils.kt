package eu.wedgess.mihole.utils.vico

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.textComponent
import eu.wedgess.mihole.data.model.PiHoleClientsOverTimeData
import eu.wedgess.mihole.ui.theme.domainsOnAdListBackground
import eu.wedgess.mihole.ui.theme.totalQueriesBackground
import eu.wedgess.mihole.utils.ColorGenerator

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

@Composable
fun rememberClientLegend(clientData: List<PiHoleClientsOverTimeData.ClientData>) = horizontalLegend(
    items =
    clientData.map {
        LegendItem(
            icon = shapeComponent(
                shape = Shapes.pillShape,
                color = ColorGenerator(isLightTheme = false).generateColorComposable(str = it.ip+it.name)
            ),
            label = textComponent(),
            labelText = "${it.ip} : ${it.name}"
        )
    },
    iconSize = 18.dp,
    iconPadding = 8.dp,
    spacing = 120.dp
)