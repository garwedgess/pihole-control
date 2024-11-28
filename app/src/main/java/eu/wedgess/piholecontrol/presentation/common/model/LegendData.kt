package eu.wedgess.piholecontrol.presentation.common.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import eu.wedgess.piholecontrol.utils.ColorGenerator

data class LegendData(
    val title: String,
    val subTitle: String? = null,
    val isSelected: Boolean = false,
    val color: Color? = null
) {
    @Composable
    fun color(): Color = color ?: ColorGenerator(isSystemInDarkTheme()).generateColor(title)

}