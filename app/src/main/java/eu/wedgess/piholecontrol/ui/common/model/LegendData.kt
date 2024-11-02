package eu.wedgess.piholecontrol.ui.common.model

import androidx.compose.ui.graphics.Color

data class LegendData(
    val title: String,
    val subTitle: String? = null,
    val color: Color,
    val isSelected: Boolean = false
)