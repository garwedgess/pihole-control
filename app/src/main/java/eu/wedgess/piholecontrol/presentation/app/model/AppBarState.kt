package eu.wedgess.piholecontrol.presentation.app.model

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.utils.UiText

sealed interface AppBarState {

    val title: UiText
    val currentConnection: ConnectionEntity?
    val adBlockingEnabled: Boolean
    val connections: List<ConnectionEntity>?
    val showNavigateBackIcon: Boolean
    val displayConnection: Boolean
    val bottomBarVisible: Boolean
    val showSettingsAction: Boolean

    data class Normal(
        override val title: UiText = UiText.DynamicString(""),
        override val currentConnection: ConnectionEntity? = null,
        override val adBlockingEnabled: Boolean = true,
        override val connections: List<ConnectionEntity>? = null,
        override val showNavigateBackIcon: Boolean = false,
        override val displayConnection: Boolean = !showNavigateBackIcon,
        val actions: (@Composable RowScope.() -> Unit)? = null,
        val overflowActions: (@Composable ColumnScope.(dismiss: () -> Unit) -> Unit)? = null,
        override val bottomBarVisible: Boolean = true,
        override val showSettingsAction: Boolean = bottomBarVisible
    ) : AppBarState

    data class Search(
        override val title: UiText = UiText.DynamicString(""),
        override val currentConnection: ConnectionEntity? = null,
        override val adBlockingEnabled: Boolean = true,
        override val connections: List<ConnectionEntity>? = null,
        override val showNavigateBackIcon: Boolean = false,
        override val displayConnection: Boolean = !showNavigateBackIcon,
        val searchContent: @Composable () -> Unit,
        override val bottomBarVisible: Boolean = true,
        override val showSettingsAction: Boolean = bottomBarVisible
    ) : AppBarState

    data class Contextual(
        val selectedCount: Int,
        val onDismiss: () -> Unit,
        val actions: @Composable RowScope.() -> Unit,
        override val title: UiText,
        override val currentConnection: ConnectionEntity? = null,
        override val adBlockingEnabled: Boolean = true,
        override val connections: List<ConnectionEntity>? = null,
        override val showNavigateBackIcon: Boolean = false,
        override val displayConnection: Boolean = false,
        override val bottomBarVisible: Boolean = false,
        override val showSettingsAction: Boolean = false
    ) : AppBarState
}

fun AppBarState.withAppInfo(
    adBlockingEnabled: Boolean,
    currentConnection: ConnectionEntity?,
    connections: List<ConnectionEntity>
): AppBarState = when (this) {
    is AppBarState.Contextual -> copy(
        adBlockingEnabled = adBlockingEnabled,
        currentConnection = currentConnection,
        connections = connections
    )
    is AppBarState.Normal -> copy(
        adBlockingEnabled = adBlockingEnabled,
        currentConnection = currentConnection,
        connections = connections
    )
    is AppBarState.Search -> copy(
        adBlockingEnabled = adBlockingEnabled,
        currentConnection = currentConnection,
        connections = connections
    )
}
