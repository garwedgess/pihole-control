package eu.wedgess.piholecontrol.ui.base

import androidx.compose.ui.graphics.vector.ImageVector
import eu.wedgess.piholecontrol.utils.UiText

abstract class TabItem(
    open val title: UiText,
    open val icon: ImageVector
)