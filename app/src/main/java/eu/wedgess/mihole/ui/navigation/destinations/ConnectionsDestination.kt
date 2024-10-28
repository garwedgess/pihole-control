package eu.wedgess.mihole.ui.navigation.destinations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.connections.all.ConnectionsContract
import eu.wedgess.mihole.ui.connections.all.view.ConnectionsScreen
import eu.wedgess.mihole.ui.connections.all.viewmodel.ConnectionsViewModel
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.utils.UiText
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.ConnectionsDestination(
    onComposing: (AppBarState) -> Unit,
    navigateToModifyConnection: (id: Long?) -> Unit,
    navigateToCreateConnection: () -> Unit
) {
    composable(
        route = Screens.Connections.route
    ) {
        val viewModel: ConnectionsViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            onComposing(
                AppBarState(
                    title = UiText.StringResource(id = R.string.appbar_title_connections),
                    showNavigateBackIcon = true,
                    bottomBarVisible = false
                )
            )
            viewModel.onEvent(ConnectionsContract.Event.FetchConnections)
        }

        LaunchedEffect(viewModel.effect) {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    is ConnectionsContract.Effect.Navigation -> {
                        if (effect is ConnectionsContract.Effect.Navigation.Edit) {
                            navigateToModifyConnection(effect.id)
                        } else {
                            navigateToCreateConnection()
                        }
                    }
                }
            }
        }

        ConnectionsScreen(
            uiState,
            viewModel::onEvent
        )
    }
}