package eu.wedgess.piholecontrol.presentation.statistics.common.components.donutchart.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal val STROKE_SIZE_UNSELECTED = 45.dp
internal val STROKE_SIZE_SELECTED = 60.dp

class DonutChartState(
    val state: State = State.Unselected
) {
    val stroke: Dp
        get() = when (state) {
            State.Selected -> STROKE_SIZE_SELECTED
            State.Unselected -> STROKE_SIZE_UNSELECTED
        }

    enum class State {
        Selected, Unselected
    }
}
