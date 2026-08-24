package eu.wedgess.piholecontrol.presentation.app.model

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.utils.UiText

data class AppBarState(
    val title: UiText = UiText.DynamicString(""),
    val currentConnection: ConnectionEntity? = null,
    val adBlockingEnabled: Boolean = true,
    val connections: List<ConnectionEntity>? = null,
    val showNavigateBackIcon: Boolean = false,
    val displayConnection: Boolean = !showNavigateBackIcon,
    val actions: (@Composable RowScope.() -> Unit)? = null,
    val showSearchView: Boolean = false,
    val searchContent: (@Composable () -> Unit)? = null,
    val bottomBarVisible: Boolean = true
)
