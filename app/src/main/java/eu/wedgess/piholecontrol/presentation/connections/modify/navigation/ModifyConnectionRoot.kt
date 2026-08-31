package eu.wedgess.piholecontrol.presentation.connections.modify.navigation

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.compose.CollectSideEffect
import eu.wedgess.piholecontrol.presentation.connections.modify.ModifyConnectionsContract
import eu.wedgess.piholecontrol.presentation.connections.modify.view.ModifyConnectionScreen
import eu.wedgess.piholecontrol.presentation.connections.modify.viewmodel.ModifyConnectionViewModel
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.utils.UiText

@ExperimentalGetImage
fun NavGraphBuilder.modifyConnectionRoot(
    onComposing: (AppBarState) -> Unit,
    onNavigateBack: () -> Boolean
) {
    composable<Screens.ModifyConnection> { backStackEntry ->
        val viewModel: ModifyConnectionViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val connectionId = backStackEntry.toRoute<Screens.ModifyConnection>().connectionId

        LaunchedEffect(Unit) {
            onComposing(
                AppBarState.Normal(
                    title = UiText.StringResource(id = R.string.appbar_title_connection),
                    showNavigateBackIcon = true,
                    bottomBarVisible = false
                )
            )
            connectionId?.run {
                viewModel.onEvent(ModifyConnectionsContract.Event.FetchCurrentConnection)
            }
        }

        CollectSideEffect(viewModel.sideEffect) { effect ->
            when (effect) {
                is ModifyConnectionsContract.Effect.Navigation.Back -> onNavigateBack()
            }
        }

        ModifyConnectionScreen(
            uiState,
            viewModel::onEvent
        )
    }
}
