package eu.wedgess.mihole.ui.base

import androidx.compose.ui.graphics.vector.ImageVector
import eu.wedgess.mihole.utils.UiText

abstract class TabItem(
    open val title: UiText,
    open val icon: ImageVector
)