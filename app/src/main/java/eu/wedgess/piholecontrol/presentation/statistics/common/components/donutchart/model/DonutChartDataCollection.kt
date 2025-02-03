package eu.wedgess.piholecontrol.presentation.statistics.common.components.donutchart.model

data class DonutChartDataCollection(
    var items: List<DonutChartData>
) {
    internal var totalAmount: Float = items.sumOf { it.percentage.toDouble() }.toFloat()
        private set
}
