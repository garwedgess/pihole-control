package eu.wedgess.mihole.ui.statistics.view.common

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class PieChartData(val title: String, val percentage: Float, val color: Color)
