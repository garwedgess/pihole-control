package eu.wedgess.mihole.ui.dashboard.view.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.horizontal.HorizontalAxis
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import eu.wedgess.mihole.data.model.PiHoleClientsOverTimeData
import eu.wedgess.mihole.ui.common.LegendData
import eu.wedgess.mihole.ui.common.LegendGridImpl
import eu.wedgess.mihole.utils.ColorGenerator
import eu.wedgess.mihole.utils.extensions.formatMilliseconds
import eu.wedgess.mihole.utils.extensions.toColorInt
import eu.wedgess.mihole.utils.vico.rememberMarker
import timber.log.Timber
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClientQueriesOvertimeGraph(
    overTimeData: PiHoleClientsOverTimeData
) {

    val darkTheme = isSystemInDarkTheme()
    val colorGenerator = remember {
        ColorGenerator(isLightTheme = !darkTheme)
    }

    val axisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { i, _ ->
        i.toLong().formatMilliseconds()
    }
    val axisYValueFormatter = AxisValueFormatter<AxisPosition.Vertical.Start> { value, _ ->
        if (value.isFinite() && value.roundToInt().toFloat() == value) value.roundToInt()
            .toString() else ""
    }

    val clientsOverTimeDataMapped = remember(overTimeData.clientsOverTime) {
        val newMap: MutableMap<PiHoleClientsOverTimeData.ClientData, List<Pair<Float, Int>>> =
            mutableMapOf()

        val keys = overTimeData.clientsOverTime.keys
        val clientIndexes = overTimeData.clientsOverTime.values.first()
        val clients = clientIndexes.mapIndexed { index, _ ->
            overTimeData.clients[index].copy(index = index)
        }

        clients.forEach { client ->
            newMap[client] = keys.map {
                Pair(
                    it,
                    requireNotNull(overTimeData.clientsOverTime[it]?.get(client.index))
                    { "Cannot find client at index: ${client.index}" }
                )
            }
        }
        newMap
    }

    val clientsEntry = remember(clientsOverTimeDataMapped) {
        clientsOverTimeDataMapped.map { (client, clientActivityList) ->
            clientActivityList.map {
                FloatEntry(it.first * 1000L, it.second.toFloat())
            }
        }
    }

    Surface(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(4.dp)
    ) {
        Column {
            Chart(
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                marker = rememberMarker(showZero = false),
                chart = lineChart(
                    lines = overTimeData.clients.map {
                        Timber.d("Client: ${it.ip + it.name}")
                        with(colorGenerator.generateColorComposable(str = it.ip + it.name + it.hashCode())) {
                            LineChart.LineSpec(
                                lineColor = this.toColorInt(),
                                lineBackgroundShader = verticalGradient(
                                    arrayOf(
                                        this.copy(alpha = 0.8f),
                                        this.copy(alpha = 0.6f),
                                        this.copy(alpha = 0.3f),
                                        this.copy(alpha = 0.1f)
                                    )
                                )
                            )
                        }
                    }
                ),
                model = entryModelOf(*clientsEntry.toTypedArray()),
                startAxis = startAxis(
                    valueFormatter = axisYValueFormatter
                ),
                bottomAxis = bottomAxis(
                    tickPosition = HorizontalAxis.TickPosition.Center(
                        spacing = clientsEntry.size.times(3)
                    ),
                    valueFormatter = axisValueFormatter
                ),
                isZoomEnabled = true,
                chartScrollSpec = rememberChartScrollSpec(isScrollEnabled = false)
            )

            LegendGridImpl(legendData = overTimeData.clients.map {
                LegendData(
                    title = it.ip,
                    subTitle = it.name,
                    color = colorGenerator.generateColor(it.ip + it.name)
                )
            })
        }
    }
}