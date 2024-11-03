package eu.wedgess.piholecontrol.ui.statistics.view.donutchart.model

import androidx.compose.ui.graphics.Color

data class DonutChartData(
    val amount: Float,
    val color: Color,
    val title: String,
)