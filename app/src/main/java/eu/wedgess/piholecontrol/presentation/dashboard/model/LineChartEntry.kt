package eu.wedgess.piholecontrol.presentation.dashboard.model

data class LineChartEntry(
    val x: Float,
    val y: Float,
    val xDisplayValue: String? = null,
    val yDisplayValue: String? = null,
    val xLabel: String? = null,
    val yLabel: String? = null
)
