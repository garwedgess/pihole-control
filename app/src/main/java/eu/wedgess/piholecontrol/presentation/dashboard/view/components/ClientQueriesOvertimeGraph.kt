package eu.wedgess.piholecontrol.presentation.dashboard.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.presentation.common.LegendGrid
import eu.wedgess.piholecontrol.presentation.common.model.LegendData
import eu.wedgess.piholecontrol.presentation.dashboard.model.LineChartInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun ClientQueriesOvertimeGraph(clientsOvertimeChartInfo: List<LineChartInfo.ClientsOverLineChart>) {
    Column {
        LineChart(
            modifier = Modifier
                .fillMaxSize()
                .heightIn(min = PiHoleControlTheme.dimens.size.lineChartHeight),
            data = clientsOvertimeChartInfo
        )

        LegendGrid(
            data = clientsOvertimeChartInfo.map { linesChartData ->
                LegendData(
                    title = linesChartData.title.asString(),
                    subTitle = linesChartData.legendSubTitle.asString(),
                    color = linesChartData.color()
                )
            }
        )
    }
}
