package eu.wedgess.piholecontrol.ui.statistics.view.donutchart.model

data class DonutChartDataCollection(
    var items: List<DonutChartData>
) {
    internal var totalAmount: Float = items.sumOf { it.amount.toDouble() }.toFloat()
        private set
}