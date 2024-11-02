package eu.wedgess.piholecontrol.ui.dashboard.model

import androidx.compose.ui.graphics.Color
import com.patrykandpatrick.vico.core.entry.ChartEntry
import eu.wedgess.piholecontrol.utils.UiText

typealias Coordinate = Pair<Number, Number>

class Entry(
    override val x: Float,
    override val y: Float,
    val xDisplayValue: String? = null,
    val yDisplayValue: String? = null,
    val xLabel: String? = null,
    val yLabel: String? = null,
) : ChartEntry {
    override fun withY(y: Float): ChartEntry = Entry(x, y, xDisplayValue, yDisplayValue, xLabel, yLabel)
}

data class LineChartData(
    val label: UiText,
    val data: Iterable<Coordinate>,
    val color: Color? = null,
)