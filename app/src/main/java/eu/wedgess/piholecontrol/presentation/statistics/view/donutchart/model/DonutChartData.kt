package eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model

import androidx.compose.ui.graphics.Color
import eu.wedgess.piholecontrol.utils.ColorGenerator

sealed class DonutChartData(
    open val percentage: Float,
    open val title: String,
) {
    fun color(isDarkTheme: Boolean): Color = ColorGenerator(isDarkTheme).generateColor(title)
}
