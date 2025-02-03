package eu.wedgess.piholecontrol.presentation.statistics.common.components.donutchart.model

data class QueryTypeChartData(override val title: String, override val percentage: Float) :
    DonutChartData(title = title, percentage = percentage)
