package eu.wedgess.piholecontrol.presentation.dashboard.view.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import com.jakewharton.threetenabp.AndroidThreeTen
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.Zoom
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineModel
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.m3.common.rememberM3VicoTheme
import eu.wedgess.piholecontrol.domain.model.OverTimeEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.dashboard.model.LineChartInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.utils.vico.rememberMarker
import eu.wedgess.piholecontrol.utils.vico.xDisplayValuesKey
import eu.wedgess.piholecontrol.utils.vico.yDisplayLabelsKey
import kotlin.math.roundToInt

@Composable
fun LineChart(
    data: Iterable<LineChartInfo>,
    modifier: Modifier = Modifier
) {
    val dataList = remember(data) { data.toList() }
    val entries = remember(dataList) { dataList.map { it.entries.toList() } }
    val colors = dataList.map { it.color() }
    val xDisplayValues = remember(entries) {
        entries.firstOrNull()
            .orEmpty()
            .associate { it.x.roundToInt() to (it.xDisplayValue ?: it.x.roundToInt().toString()) }
    }
    val yDisplayLabels = remember(entries) {
        entries.map { series -> series.firstOrNull()?.yLabel }
    }
    val horizontalLabelSpacing = remember(entries) {
        maxOf(entries.maxOfOrNull { it.size }?.div(HORIZONTAL_AXIS_LABEL_COUNT) ?: 1, 1)
    }

    val chartModelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(entries, xDisplayValues, yDisplayLabels) {
        chartModelProducer.runTransaction {
            lineModel {
                entries.forEach { series ->
                    series(
                        x = series.map { it.x },
                        y = series.map { it.y }
                    )
                }
            }
            extras {
                it[xDisplayValuesKey] = xDisplayValues
                it[yDisplayLabelsKey] = yDisplayLabels
            }
        }
    }

    ProvideVicoTheme(rememberM3VicoTheme(lineCartesianLayerColors = colors)) {
        CartesianChartHost(
            modifier = modifier,
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(
                    lineProvider = LineCartesianLayer.LineProvider.series(
                        colors.map { color ->
                            LineCartesianLayer.rememberLine(
                                fill = LineCartesianLayer.LineFill.single(Fill(color)),
                                areaFill = LineCartesianLayer.AreaFill.single(
                                    Fill(
                                        Brush.verticalGradient(
                                            listOf(
                                                color.copy(alpha = 0.9f),
                                                color.copy(alpha = 0.5f),
                                                color.copy(alpha = 0.2f)
                                            )
                                        )
                                    )
                                )
                            )
                        }
                    )
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    tick = null,
                    itemPlacer = remember(horizontalLabelSpacing) {
                        HorizontalAxis.ItemPlacer.aligned(
                            spacing = { horizontalLabelSpacing },
                            addExtremeLabelPadding = false
                        )
                    },
                    guideline = null,
                    valueFormatter = remember {
                        CartesianValueFormatter { context, value, _ ->
                            context.model.extraStore.getOrNull(xDisplayValuesKey)
                                ?.get(value.roundToInt())
                                ?: value.roundToInt().toString()
                        }
                    }
                ),
                startAxis = VerticalAxis.rememberStart(
                    itemPlacer = remember { VerticalAxis.ItemPlacer.count({ VERTICAL_AXIS_LABEL_COUNT }) },
                    tick = null,
                    guideline = null,
                    horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
                    valueFormatter = remember {
                        CartesianValueFormatter { _, value, _ -> value.roundToInt().toString() }
                    }
                ),
                marker = rememberMarker(),
                getXStep = { _, _, _ -> 1.0 }
            ),
            modelProducer = chartModelProducer,
            scrollState = rememberVicoScrollState(scrollEnabled = false),
            zoomState = rememberVicoZoomState(zoomEnabled = false, initialZoom = Zoom.Content),
            animateIn = false
        )
    }
}

private const val HORIZONTAL_AXIS_LABEL_COUNT = 6
private const val VERTICAL_AXIS_LABEL_COUNT = 5

@ThemePreview
@Composable
private fun LineChartPreview() {
    AndroidThreeTen.init(LocalContext.current)
    PiHoleControlTheme {
        Surface {
            LineChart(
                modifier = Modifier.fillMaxSize(),
                data = listOf(
                    LineChartInfo.PermittedQueriesOverLineChart(
                        entity = listOf(
                            OverTimeEntity(1525546500, 84),
                            OverTimeEntity(1525547100, 154),
                            OverTimeEntity(1525547700, 50)
                        )
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
