package eu.wedgess.piholecontrol.presentation.dashboard.view.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import eu.wedgess.piholecontrol.domain.model.OverTimeEntity
import eu.wedgess.piholecontrol.presentation.common.previews.ThemePreview
import eu.wedgess.piholecontrol.presentation.dashboard.model.LineChartEntry
import eu.wedgess.piholecontrol.presentation.dashboard.model.LineChartInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.utils.extensions.toColorInt
import eu.wedgess.piholecontrol.utils.vico.rememberMarker
import kotlin.math.roundToInt

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    data: Iterable<LineChartInfo>
) = ProvideChartStyle(
    m3ChartStyle(entityColors = data.map { it.color() })
) {

    val entries = remember(data) {
        data.map { it.entries.toList() }
    }

    val chartModelProducer = remember {
        ChartEntryModelProducer(entries)
    }

    LaunchedEffect(entries) {
        chartModelProducer.setEntries(entries)
    }

    Chart(modifier = modifier,
        chart = lineChart(
            lines = data.map {
                with(it.color()) {
                    LineChart.LineSpec(
                        lineColor = this.toColorInt(),
                        lineBackgroundShader = verticalGradient(
                            arrayOf(
                                this.copy(alpha = 0.9f),
                                this.copy(alpha = 0.5f),
                                this.copy(alpha = 0.2f)
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
                maxOf(data.maxOf { it.entries.count() } / 5, 1).let {
                    AxisItemPlacer.Horizontal.default(spacing = it, addExtremeLabelPadding = true)
                }
            },
            guideline = null,
            valueFormatter = { value, chartValues ->
                (chartValues.chartEntryModel.entries.firstOrNull()
                    ?.find { it.x == value } as? LineChartEntry)?.xDisplayValue ?: ""
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
    PiHoleControlTheme {
        Surface {
            LineChart(
                modifier = Modifier.fillMaxSize(),
                data = listOf(
                    LineChartInfo.PermittedQueriesOverLineChart(
                        entity = listOf(
                            OverTimeEntity(1525546500, 163),
                            OverTimeEntity(1525547100, 154),
                            OverTimeEntity(1525547700, 164)
                        ),
                    ),
                    LineChartInfo.BlockedQueriesOverLineChart(
                        entity = listOf(
                            OverTimeEntity(1525546500, 30),
                            OverTimeEntity(1525547100, 64),
                            OverTimeEntity(1525547700, 10)
                        )
                    )
                )
            )
        }
    }
}