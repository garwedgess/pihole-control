package eu.wedgess.piholecontrol.presentation.statistics.tabs.destinations.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.common.LegendGrid
import eu.wedgess.piholecontrol.presentation.common.model.LegendData
import eu.wedgess.piholecontrol.presentation.compose.ScrollAdaptiveColumn
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.DonutChart
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model.DonutChartDataCollection
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model.ForwardDestinationsChartData
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun ForwardDestinationsContent(
    chartDataCollection: DonutChartDataCollection,
    legendData: List<LegendData>,
    onSelectedIndex: (Int) -> Unit
) {
    Card(
        modifier = Modifier.padding(PiHoleControlTheme.dimens.padding.screenContent)
    ) {
        ScrollAdaptiveColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PiHoleControlTheme.dimens.padding.itemContent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                PiHoleControlTheme.dimens.padding.itemContentSmall,
                Alignment.CenterVertically
            )
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.statistics_forward_destinations_title),
                style = MaterialTheme.typography.titleMedium
            )
            DonutChart(
                data = chartDataCollection,
                chartSize = PiHoleControlTheme.dimens.size.pieChart,
                onSelectedIndexChange = onSelectedIndex
            )
            LegendGrid(
                data = legendData,
                modifier = Modifier
                    .padding(horizontal = PiHoleControlTheme.dimens.padding.itemContent)
            )
        }
    }
}

@ThemePreview
@Composable
private fun ForwardDestinationContentPreview() {
    PiHoleControlTheme {
        Surface {
            ForwardDestinationsContent(
                chartDataCollection = DonutChartDataCollection(
                    items = listOf(
                        ForwardDestinationsChartData("blocked|blocked", 16.5f),
                        ForwardDestinationsChartData("cached|cached", 27.5f),
                        ForwardDestinationsChartData("other|other", 26f),
                        ForwardDestinationsChartData("localhost|127.0.0.1", 30f)
                    )
                ),
                legendData = listOf(
                    LegendData("blocked|blocked", "16.5%"),
                    LegendData("cached|cached", "27.5%"),
                    LegendData("other|other", "26%"),
                    LegendData("localhost|127.0.0.1", "30%")
                ),
                onSelectedIndex = {}
            )
        }
    }
}
