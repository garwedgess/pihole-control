package eu.wedgess.mihole.ui.statistics.view.content

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.common.LegendData
import eu.wedgess.mihole.ui.common.LegendGridImpl
import eu.wedgess.mihole.ui.common.previews.ThemePreview
import eu.wedgess.mihole.ui.statistics.view.common.PieChart
import eu.wedgess.mihole.ui.statistics.view.common.PieChartData
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.utils.ColorGenerator

@Composable
fun ForwardDestinationsContent(
    forwardDestinations: Map<String, Float>,
    colorGenerator: ColorGenerator
) {

    var selectedIndex by remember {
        mutableIntStateOf(-1)
    }
    val pieChartData = remember(forwardDestinations) {
        forwardDestinations.map { (key, value) ->
            PieChartData(
                title = key,
                percentage = value,
                color = colorGenerator.generateColor(key)
            )
        }
    }

    val legendData = remember(pieChartData, selectedIndex) {
        pieChartData.mapIndexed { index, pieChartData ->
            LegendData(
                title = pieChartData.title,
                subTitle = "${pieChartData.percentage}%",
                color = pieChartData.color,
                isSelected = selectedIndex == index
            )
        }
    }

    Card(
        modifier = Modifier.padding(MiHoleTheme.dimens.padding.screenContent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MiHoleTheme.dimens.padding.itemContent)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                MiHoleTheme.dimens.padding.itemContentSmall,
                Alignment.CenterVertically
            )
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.statistics_forward_destinations_title),
                style = MaterialTheme.typography.titleMedium
            )
            PieChart(
                modifier = Modifier.size(MiHoleTheme.dimens.size.pieChart),
                data = pieChartData,
                onClick = { pieItem, index ->
                    selectedIndex = index
                }
            )
            LegendGridImpl(
                legendData = legendData,
                modifier = Modifier
                    .padding(horizontal = MiHoleTheme.dimens.padding.itemContent)
            )
        }
    }
}

@ThemePreview
@Composable
private fun ForwardDestinationContentPreview() {
    MiHoleTheme {
        ForwardDestinationsContent(
            forwardDestinations = mapOf(
                "blocked|blocked" to 16.5f,
                "cached|cached" to 27.5f,
                "other|other" to 26f,
                "localhost|127.0.0.1" to 30f
            ),
            colorGenerator = ColorGenerator(
                isSystemInDarkTheme().not()
            )
        )
    }
}