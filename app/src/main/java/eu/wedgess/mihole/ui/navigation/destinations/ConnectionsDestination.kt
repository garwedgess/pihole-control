package eu.wedgess.mihole.ui.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.app.model.AppBarState
import eu.wedgess.mihole.ui.compose.CollectSideEffect
import eu.wedgess.mihole.ui.connections.list.ConnectionsContract
import eu.wedgess.mihole.ui.connections.list.view.ConnectionsScreen
import eu.wedgess.mihole.ui.connections.list.viewmodel.ConnectionsViewModel
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.utils.UiText

fun NavGraphBuilder.ConnectionsDestination(
    onComposing: (AppBarState) -> Unit,
    navigateToModifyConnection: (id: Long?) -> Unit,
    navigateToCreateConnection: () -> Unit
) {
    composable(
        route = Screens.Connections.route
    ) {
        val viewModel: ConnectionsViewModel = hiltViewModel()
        val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            onComposing(
                AppBarState(
                    title = UiText.StringResource(id = R.string.appbar_title_connections),
                    showNavigateBackIcon = true,
                    bottomBarVisible = false
                )
            )
        }

        CollectSideEffect(viewModel.sideEffect) { effect ->
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

        ConnectionsScreen(uiResult = uiResult, onEvent = viewModel::onEvent)
    }
}