package eu.wedgess.piholecontrol.presentation.statistics.tabs.querytypes.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model.QueryTypeChartData
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun QueryTypesContent(
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
                .padding(PiHoleControlTheme.dimens.padding.itemContent)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                PiHoleControlTheme.dimens.padding.itemContentSmall,
                Alignment.CenterVertically
            )
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.statistics_query_types_title),
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
                    .padding(horizontal = PiHoleControlTheme.dimens.padding.screenContent)
            )
        }
    }
}

@ThemePreview
@Composable
private fun QueryTypesContentPreview() {
    PiHoleControlTheme {
        Surface {
            QueryTypesContent(
                chartDataCollection = DonutChartDataCollection(
                    items = listOf(
                        QueryTypeChartData(title = "AIPv4", percentage = 60f),
                        QueryTypeChartData(title = "Any", percentage = 1f),
                        QueryTypeChartData(title = "NAPTR", percentage = 5f),
                        QueryTypeChartData(title = "PTR", percentage = 4f),
                        QueryTypeChartData(title = "DS", percentage = 10f),
                        QueryTypeChartData(title = "TXT", percentage = 10f),
                        QueryTypeChartData(title = "HTTPS", percentage = 10f)
                    )
                ),
                legendData = listOf(
                    LegendData(title = "AIPv4", subTitle = "60%"),
                    LegendData(title = "Any", subTitle = "1%"),
                    LegendData(title = "NAPTR", subTitle = "5%"),
                    LegendData(title = "PTR", subTitle = "4%"),
                    LegendData(title = "DS", subTitle = "10%"),
                    LegendData(title = "TXT", subTitle = "10%"),
                    LegendData(title = "HTTPS", subTitle = "10%")
                ),
                onSelectedIndex = {}
            )
        }
    }
}
