package eu.wedgess.piholecontrol.presentation.connections.list.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.compose.CollectSideEffect
import eu.wedgess.piholecontrol.presentation.connections.list.ConnectionsContract
import eu.wedgess.piholecontrol.presentation.connections.list.view.ConnectionsScreen
import eu.wedgess.piholecontrol.presentation.connections.list.viewmodel.ConnectionsViewModel
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.utils.UiText

fun NavGraphBuilder.connectionsRoot(
    onComposing: (AppBarState) -> Unit,
    navigateToModifyConnection: (id: Long?) -> Unit,
    navigateToCreateConnection: () -> Unit
) {
    composable<Screens.Connections> {
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