package eu.wedgess.mihole.ui.dashboard.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
                        with(colorGenerator.generateColor(str = it.ip + it.name)) {
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
//            legend = rememberClientLegend(overTimeData.clients),
                isZoomEnabled = true,
                chartScrollSpec = rememberChartScrollSpec(isScrollEnabled = false)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                overTimeData.clients.forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(
                                    colorGenerator.generateColor(
                                        str = it.ip + it.name
                                    )
                                )
                        )
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            it.name.takeIf { it.isNotBlank() }?.run {
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Text(
                                text = it.ip,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                    }
                }
            }
        }
    }
}