package eu.wedgess.mihole.ui.dashboard.view.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.horizontal.HorizontalAxis
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import eu.wedgess.mihole.ui.dashboard.model.Entry
import eu.wedgess.mihole.ui.dashboard.model.LineChartData
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.ui.theme.totalQueriesBackground
import eu.wedgess.mihole.utils.UiText
import eu.wedgess.mihole.utils.vico.rememberMarker
import java.text.DateFormat
import kotlin.math.roundToInt

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    data: Iterable<LineChartData>,
    xAxisFormatter: ((y: Number) -> String)? = null
) = ProvideChartStyle(
    m3ChartStyle(entityColors = data.map { it.color ?: MaterialTheme.colorScheme.primary })
) {
    val context = LocalContext.current
    val entries = remember(data) {
        data.map { lineData ->
            lineData.data.mapIndexed { index, coordinate ->
                Entry(
                    if (xAxisFormatter == null) coordinate.first.toFloat() else index.toFloat(),
                    coordinate.second.toFloat(),
                    xDisplayValue = xAxisFormatter?.invoke(coordinate.first),
                    yLabel = lineData.label.asString(context)
                )
            }
        }
    }

    val chartModelProducer = remember { ChartEntryModelProducer(entries) }

    LaunchedEffect(entries) {
        chartModelProducer.setEntries(entries)
    }

    Chart(modifier = modifier,
        chart = lineChart(),
        chartModelProducer = chartModelProducer,
        bottomAxis = bottomAxis(
            tickPosition = maxOf(data.maxOf { it.data.count() / 4 }, 1).let {
                HorizontalAxis.TickPosition.Center(it, it)
            },
            guideline = null,
            valueFormatter = { value, chartValues ->
                (chartValues.chartEntryModel.entries.firstOrNull()
                    ?.getOrNull(value.toInt()) as Entry?)?.xDisplayValue
                    ?: value.toString()
            },
        ),
        startAxis = startAxis(
            guideline = null,
            valueFormatter = { value, _ ->
                if (value == 0f) "" else value.roundToInt().toString()
            },
            horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
            maxLabelCount = 4
        ),
        chartScrollSpec = rememberChartScrollSpec(isScrollEnabled = false),
        marker = rememberMarker()
    )
}

@Preview(showBackground = true)
@Composable
fun LineChartPreview() {
    val formatter = DateFormat.getDateInstance()
    MiHoleTheme {
        LineChart(
            modifier = Modifier.fillMaxSize(),
            data = listOf(
                LineChartData(
                    label = UiText.DynamicString("label"),
                    data = listOf(1525546500 to 163, 1525547100 to 154, 1525547700 to 164),
                    color = MaterialTheme.colorScheme.totalQueriesBackground
                ), LineChartData(
                    label = UiText.DynamicString("label"),
                    data = listOf(1525546500 to 30, 1525547100 to 64, 1525547700 to 10),
                    color = MaterialTheme.colorScheme.error
                )
            ),
            xAxisFormatter = { formatter.format(it) })
    }
}