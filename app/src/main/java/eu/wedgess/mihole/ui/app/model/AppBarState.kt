package eu.wedgess.mihole.ui.app.model

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.utils.UiText

data class AppBarState(
    val title: UiText = UiText.DynamicString(""),
    val currentConnection: PiHoleInfo? = null,
    val adBlockingEnabled: Boolean = true,
    val connections: List<PiHoleInfo>? = null,
    val showNavigateBackIcon: Boolean = false,
    val actions: (@Composable RowScope.() -> Unit)? = null,
    val showSearchView: Boolean = false,
    val searchContent: (@Composable () -> Unit)? = null,
    val bottomBarVisible: Boolean = true
) {
    val displayConnection: Boolean
        get() {
            return !showNavigateBackIcon
        }
}