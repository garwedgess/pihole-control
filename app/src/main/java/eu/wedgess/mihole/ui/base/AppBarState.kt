package eu.wedgess.mihole.ui.base

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.utils.UiText

data class AppBarState(
    val title: UiText = UiText.DynamicString(""),
    val currentConnection: MiHolesInfo? = null,
    val adBlockingEnabled: Boolean = true,
    val connections: List<MiHolesInfo>? = null,
    val showNavigateBackIcon: Boolean = false,
    val actions: (@Composable RowScope.() -> Unit)? = null,
    val showSearchView: Boolean = false,
    val searchContent: (@Composable () -> Unit)? = null
) {
    val displayConnection: Boolean
        get() {
            return !showNavigateBackIcon
        }
}