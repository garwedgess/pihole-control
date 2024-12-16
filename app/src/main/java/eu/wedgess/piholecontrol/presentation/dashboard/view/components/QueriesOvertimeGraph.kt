package eu.wedgess.piholecontrol.presentation.dashboard.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.common.LegendGrid
import eu.wedgess.piholecontrol.presentation.common.model.LegendData
import eu.wedgess.piholecontrol.presentation.dashboard.model.OverTimeLineChartInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun QueriesOvertimeGraph(overTimeData: OverTimeLineChartInfo) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LineChart(
            modifier = Modifier
                .fillMaxSize()
                .heightIn(min = PiHoleControlTheme.dimens.size.lineChartHeight),
            data = listOf(overTimeData.permittedChartInfo, overTimeData.blockedChartInfo),
        )
        LegendGrid(
            data = listOf(
                LegendData(
                    title = stringResource(id = R.string.home_title_queries_over_time_permitted),
                    subTitle = overTimeData.permittedChartInfo.legendSubTitle.asString(),
                    color = overTimeData.permittedChartInfo.color()
                ),
                LegendData(
                    title = stringResource(id = R.string.home_title_queries_over_time_blocked),
                    subTitle = overTimeData.blockedChartInfo.legendSubTitle.asString(),
                    color = overTimeData.blockedChartInfo.color()
                )
            )
        )
    }
}
