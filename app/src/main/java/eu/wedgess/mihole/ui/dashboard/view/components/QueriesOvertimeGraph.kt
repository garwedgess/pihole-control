package eu.wedgess.mihole.ui.dashboard.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import eu.wedgess.mihole.data.model.PiHoleOverTimeData
import eu.wedgess.mihole.ui.theme.domainsOnAdListBackground
import eu.wedgess.mihole.ui.theme.totalQueriesBackground
import eu.wedgess.mihole.utils.extensions.formatMilliseconds
import eu.wedgess.mihole.utils.extensions.toColorInt
import eu.wedgess.mihole.utils.vico.rememberLegend
import eu.wedgess.mihole.utils.vico.rememberMarker
import kotlin.math.roundToInt

@Composable
fun QueriesOvertimeGraph(
    overTimeData: PiHoleOverTimeData
) {

    val axisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { i, _ ->
        i.toLong().formatMilliseconds()
    }
    val axisYValueFormatter = AxisValueFormatter<AxisPosition.Vertical.Start> { value, _ ->
        if (value.isFinite() && value.roundToInt().toFloat() == value) value.roundToInt()
            .toString() else ""
    }

    val adsEntry = remember(overTimeData.adsOverTime) {
        overTimeData.adsOverTime.map {
            FloatEntry(it.key * 1000L, it.value)
        }
    }

    val domainsEntry = remember(overTimeData.domainsOverTime) {
        overTimeData.domainsOverTime.map {
            FloatEntry(it.key * 1000L, it.value)
        }
    }

    Surface(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(4.dp)
    ) {
        Chart(
            modifier = Modifier.padding(top = 16.dp),
            marker = rememberMarker(),
            chart = lineChart(
                lines = listOf(
                    LineChart.LineSpec(
                        lineColor = MaterialTheme.colorScheme.domainsOnAdListBackground.toColorInt(),
                        lineBackgroundShader = verticalGradient(
                            arrayOf(
                                MaterialTheme.colorScheme.domainsOnAdListBackground.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.domainsOnAdListBackground.copy(alpha = 0.6f),
                                MaterialTheme.colorScheme.domainsOnAdListBackground.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.domainsOnAdListBackground.copy(alpha = 0.1f)
                            )
                        )
                    ),
                    LineChart.LineSpec(
                        lineColor = MaterialTheme.colorScheme.totalQueriesBackground.toColorInt(),
                        lineBackgroundShader = verticalGradient(
                            arrayOf(
                                MaterialTheme.colorScheme.totalQueriesBackground.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.totalQueriesBackground.copy(alpha = 0.6f),
                                MaterialTheme.colorScheme.totalQueriesBackground.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.totalQueriesBackground.copy(alpha = 0.1f)
                            )
                        )
                    )
                )
            ),
            model = entryModelOf(adsEntry, domainsEntry),
            startAxis = startAxis(
                valueFormatter = axisYValueFormatter
            ),
            bottomAxis = bottomAxis(
                tickPosition = HorizontalAxis.TickPosition.Center(
                    spacing = maxOf(
                        domainsEntry.size,
                        adsEntry.size
                    )
                ),
                valueFormatter = axisValueFormatter
            ),
            legend = rememberLegend(),
            isZoomEnabled = true,
            chartScrollSpec = rememberChartScrollSpec(isScrollEnabled = false)
        )
    }
}