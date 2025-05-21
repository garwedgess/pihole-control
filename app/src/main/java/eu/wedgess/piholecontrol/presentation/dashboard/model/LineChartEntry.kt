package eu.wedgess.piholecontrol.presentation.dashboard.model

import com.patrykandpatrick.vico.core.entry.ChartEntry

class LineChartEntry(
    override val x: Float,
    override val y: Float,
    val xDisplayValue: String? = null,
    val yDisplayValue: String? = null,
    val xLabel: String? = null,
    val yLabel: String? = null
) : ChartEntry {
    override fun withY(y: Float): ChartEntry =
        LineChartEntry(x, y, xDisplayValue, yDisplayValue, xLabel, yLabel)
}
