package eu.wedgess.mihole.ui.dashboard.view.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import eu.wedgess.mihole.ui.common.previews.ThemePreview
import eu.wedgess.mihole.ui.dashboard.model.Entry
import eu.wedgess.mihole.ui.dashboard.model.LineChartData
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.ui.theme.totalQueriesBackground
import eu.wedgess.mihole.utils.UiText
import eu.wedgess.mihole.utils.extensions.toColorInt
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
        chart = lineChart(
            lines = data.map {
                with(it.color ?: Color.Unspecified) {
                    LineChart.LineSpec(
                        lineColor = this.toColorInt(),
                        lineBackgroundShader = verticalGradient(
                            arrayOf(
                                this.copy(alpha = 0.7f),
                                this.copy(alpha = 0.5f),
                                this.copy(alpha = 0.3f),
                                this.copy(alpha = 0.1f)
                            )
                        )
                    )
                }
            }
        ),
        chartModelProducer = chartModelProducer,
        bottomAxis = rememberBottomAxis(
            tick = null,
            itemPlacer = remember {
                maxOf(data.maxOf { it.data.count() / 5 }, 1).let {
                    AxisItemPlacer.Horizontal.default(spacing = it, addExtremeLabelPadding = true)
                }
            },
            guideline = null,
            valueFormatter = { value, chartValues ->
                (chartValues.chartEntryModel.entries.firstOrNull()
                    ?.getOrNull(value.toInt()) as Entry?)?.xDisplayValue
                    ?: value.toString()
            }
        ),
        startAxis = rememberStartAxis(
            itemPlacer = remember { AxisItemPlacer.Vertical.default(maxItemCount = 5) },
            tick = null,
            guideline = null,
            horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
            valueFormatter = { value, _ ->
                if (value == 0f) "" else value.roundToInt().toString()
            }
        ),
        chartScrollSpec = rememberChartScrollSpec(isScrollEnabled = false),
        marker = rememberMarker()
    )
}

@ThemePreview
@Composable
fun LineChartPreview() {
    val formatter = DateFormat.getDateInstance()
    MiHoleTheme {
        Surface {
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
}