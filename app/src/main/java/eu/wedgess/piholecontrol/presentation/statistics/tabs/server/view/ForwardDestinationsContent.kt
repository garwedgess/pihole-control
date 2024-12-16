package eu.wedgess.piholecontrol.presentation.statistics.tabs.server.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import eu.wedgess.piholecontrol.domain.model.ForwardDestinationEntity
import eu.wedgess.piholecontrol.presentation.common.LegendGrid
import eu.wedgess.piholecontrol.presentation.common.model.LegendData
import eu.wedgess.piholecontrol.presentation.common.previews.ThemePreview
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.DonutChart
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model.DonutChartDataCollection
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model.QueryTypeChartData
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.presentation.theme.isDark
import eu.wedgess.piholecontrol.utils.ColorGenerator

@Composable
fun ForwardDestinationsContent(forwardDestinations: List<ForwardDestinationEntity>) {
    val isDarkTheme = MaterialTheme.colorScheme.isDark
    val colorGenerator = remember(isDarkTheme) {
        ColorGenerator(isLightTheme = isDarkTheme.not())
    }
    var selectedIndex by remember {
        mutableIntStateOf(-1)
    }
    val pieChartData = remember(forwardDestinations, colorGenerator) {
        DonutChartDataCollection(
            forwardDestinations.map { (key, value) ->
                QueryTypeChartData(
                    title = key,
                    percentage = value
                )
            }
        )
    }

    val legendData = remember(pieChartData, selectedIndex) {
        pieChartData.items.mapIndexed { index, pieChartData ->
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
        Column(
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
                text = stringResource(R.string.statistics_forward_destinations_title),
                style = MaterialTheme.typography.titleMedium
            )
            DonutChart(
                data = pieChartData,
                chartSize = PiHoleControlTheme.dimens.size.pieChart,
                onSelectedIndexChange = {
                    selectedIndex = it
                }
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
                forwardDestinations = listOf(
                    ForwardDestinationEntity("blocked|blocked", 16.5f),
                    ForwardDestinationEntity("cached|cached", 27.5f),
                    ForwardDestinationEntity("other|other", 26f),
                    ForwardDestinationEntity("localhost|127.0.0.1", 30f)
                )
            )
        }
    }
}
