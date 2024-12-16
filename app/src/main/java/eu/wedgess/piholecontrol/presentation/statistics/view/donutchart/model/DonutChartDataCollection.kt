package eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model

data class DonutChartDataCollection(
    var items: List<DonutChartData>
) {
    internal var totalAmount: Float = items.sumOf { it.percentage.toDouble() }.toFloat()
        private set
}
