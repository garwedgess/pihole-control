package eu.wedgess.mihole.ui.base

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

data class AppBarState(
    val title: String = "",
    val showNavigateBackIcon: Boolean = false,
    val actions: (@Composable RowScope.() -> Unit)? = null,
    val showSearchView: Boolean = false,
    val searchContent: (@Composable () -> Unit)? = null
)