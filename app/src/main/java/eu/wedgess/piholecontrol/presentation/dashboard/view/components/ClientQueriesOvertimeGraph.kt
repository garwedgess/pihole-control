package eu.wedgess.piholecontrol.presentation.dashboard.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.domain.model.ClientOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.OverTimeEntity
import eu.wedgess.piholecontrol.presentation.common.components.LegendGrid
import eu.wedgess.piholecontrol.presentation.common.model.LegendData
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.dashboard.model.LineChartInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun ClientQueriesOvertimeGraph(clientsOvertimeChartInfo: List<LineChartInfo.ClientsOverLineChart>) {
    Column {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(PiHoleControlTheme.dimens.size.lineChartHeight),
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

@ThemePreview
@Composable
private fun ClientQueriesOvertimeGraphPreview() {
    PiHoleControlTheme {
        Surface {
            ClientQueriesOvertimeGraph(
                clientsOvertimeChartInfo = listOf(
                    LineChartInfo.ClientsOverLineChart(
                        entity = ClientOverTimeEntity(
                            clientName = "Test",
                            clientIp = "192.168.61.21",
                            clientActivity = listOf(
                                OverTimeEntity(timestamp = 1734367500, hits = 167),
                                OverTimeEntity(timestamp = 1734368100, hits = 286),
                                OverTimeEntity(timestamp = 1734368700, hits = 25),
                                OverTimeEntity(timestamp = 1734369300, hits = 0),
                                OverTimeEntity(timestamp = 1734369900, hits = 92),
                                OverTimeEntity(timestamp = 1734370500, hits = 81)
                            )
                        )
                    ),
                    LineChartInfo.ClientsOverLineChart(
                        entity = ClientOverTimeEntity(
                            clientName = "Router",
                            clientIp = "192.168.61.1",
                            clientActivity = listOf(
                                OverTimeEntity(timestamp = 1734367500, hits = 267),
                                OverTimeEntity(timestamp = 1734368100, hits = 296),
                                OverTimeEntity(timestamp = 1734368700, hits = 250),
                                OverTimeEntity(timestamp = 1734369300, hits = 100),
                                OverTimeEntity(timestamp = 1734369900, hits = 92),
                                OverTimeEntity(timestamp = 1734370500, hits = 81)
                            )
                        )
                    )
                )
            )
        }
    }
}
