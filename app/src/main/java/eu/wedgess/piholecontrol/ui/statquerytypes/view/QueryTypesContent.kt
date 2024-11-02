package eu.wedgess.piholecontrol.ui.statquerytypes.view

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
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.data.model.responses.QueryTypes
import eu.wedgess.piholecontrol.ui.common.LegendGrid
import eu.wedgess.piholecontrol.ui.common.model.LegendData
import eu.wedgess.piholecontrol.ui.common.previews.ThemePreview
import eu.wedgess.piholecontrol.ui.statistics.view.common.PieChart
import eu.wedgess.piholecontrol.ui.statistics.view.common.PieChartData
import eu.wedgess.piholecontrol.ui.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.ui.theme.isDark
import eu.wedgess.piholecontrol.utils.ColorGenerator

@Composable
fun QueryTypesContent(queryTypes: QueryTypes) {

    var selectedIndex by remember {
        mutableIntStateOf(-1)
    }
    val isDarkTheme = MaterialTheme.colorScheme.isDark
    val colorGenerator = remember(isDarkTheme) {
        ColorGenerator(isLightTheme = isDarkTheme.not())
    }

    val pieChartData = remember(queryTypes) {
        queryTypes.asList().map {
            PieChartData(
                title = it.first,
                percentage = it.second,
                color = colorGenerator.generateColor(it.first)
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
                text = stringResource(R.string.statistics_query_types_title),
                style = MaterialTheme.typography.titleMedium
            )
            PieChart(
                modifier = Modifier.size(PiHoleControlTheme.dimens.size.pieChart),
                data = pieChartData,
                onClick = { pieItem, index ->
                    selectedIndex = index
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
        QueryTypesContent(
            queryTypes = QueryTypes(
                AIPv4 = 60f,
                any = 1f,
                NAPTR = 5f,
                PTR = 4f,
                DS = 10f,
                TXT = 10f,
                HTTPS = 10f
            )
        )
    }
}