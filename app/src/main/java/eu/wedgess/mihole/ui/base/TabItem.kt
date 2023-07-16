package eu.wedgess.mihole.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import eu.wedgess.mihole.utils.UiText

data class TabItem (
    val title: UiText,
    val icon: ImageVector,
    val screen: @Composable () -> Unit
)