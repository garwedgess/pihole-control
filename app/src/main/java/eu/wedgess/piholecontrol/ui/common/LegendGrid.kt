package eu.wedgess.piholecontrol.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import eu.wedgess.piholecontrol.ui.common.model.LegendData
import eu.wedgess.piholecontrol.ui.common.previews.ThemePreview

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LegendGrid(data: List<LegendData>, modifier: Modifier = Modifier) {
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 2,
        modifier = modifier.padding(top = 12.dp)
    ) {
        data.forEach { legend ->
            LegendItem(legendData = legend)
        }
    }
}

@ThemePreview
@Composable
private fun LegendGridPreview() {
    LegendGrid(
        data =
        listOf(
            LegendData(
                title = "Title1 that is really long and does not fit",
                subTitle = "Subtitle1 long",
                color = Color.Red
            ),
            LegendData(
                title = "Title2",
                subTitle = "Subtitle2 longest",
                color = Color.Green
            ),
            LegendData(title = "Title3", subTitle = "Subtitle3", color = Color.Blue),
            LegendData(title = "Title4", color = Color.Yellow),
            LegendData(title = "Title5", subTitle = "Subtitle5", color = Color.Cyan)
        )
    )
}