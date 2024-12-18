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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.common.LegendGrid
import eu.wedgess.piholecontrol.presentation.common.model.LegendData
import eu.wedgess.piholecontrol.presentation.common.previews.ThemePreview
import eu.wedgess.piholecontrol.presentation.compose.ScrollAdaptiveColumn
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.DonutChart
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model.DonutChartDataCollection
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model.QueryTypeChartData
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun QueryTypesContent(queryTypes: DonutChartDataCollection) {
    var selectedIndex by remember { mutableIntStateOf(-1) }

    val legendData = remember(queryTypes.items, selectedIndex) {
        queryTypes.items.mapIndexed { index, pieChartData ->
            LegendData(
                title = pieChartData.title,
                subTitle = "${pieChartData.percentage}%",
                isSelected = selectedIndex == index
            )
        }
    }

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
                data = queryTypes,
                chartSize = PiHoleControlTheme.dimens.size.pieChart,
                onSelectedIndexChange = {
                    selectedIndex = it
                }
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
                queryTypes = DonutChartDataCollection(
                    items = listOf(
                        QueryTypeChartData(title = "AIPv4", percentage = 60f),
                        QueryTypeChartData(title = "Any", percentage = 1f),
                        QueryTypeChartData(title = "NAPTR", percentage = 5f),
                        QueryTypeChartData(title = "PTR", percentage = 4f),
                        QueryTypeChartData(title = "DS", percentage = 10f),
                        QueryTypeChartData(title = "TXT", percentage = 10f),
                        QueryTypeChartData(title = "HTTPS", percentage = 10f)
                    )
                )
            )
        }
    }
}
