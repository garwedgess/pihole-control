package eu.wedgess.mihole.ui.statistics.view.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.data.model.PiHoleStatistics
import eu.wedgess.mihole.ui.common.LegendData
import eu.wedgess.mihole.ui.common.LegendGridImpl
import eu.wedgess.mihole.ui.statistics.view.common.PieChart
import eu.wedgess.mihole.ui.statistics.view.common.PieChartData
import eu.wedgess.mihole.utils.ColorGenerator

@Composable
fun QueryTypesContent(queryTypes: PiHoleStatistics.QueryTypes, colorGenerator: ColorGenerator) {

    var selectedIndex by remember {
        mutableStateOf(-1)
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PieChart(
            modifier = Modifier.size(250.dp),
            data = pieChartData,
            onClick = { pieItem, index ->
                selectedIndex = index
            }
        )
        LegendGridImpl(
            legendData = legendData,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
    }
}