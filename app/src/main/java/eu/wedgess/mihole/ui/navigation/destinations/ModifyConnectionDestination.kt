package eu.wedgess.mihole.ui.navigation.destinations

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.app.model.AppBarState
import eu.wedgess.mihole.ui.compose.CollectSideEffect
import eu.wedgess.mihole.ui.connections.modify.ModifyConnectionsContract
import eu.wedgess.mihole.ui.connections.modify.view.ModifyConnectionScreen
import eu.wedgess.mihole.ui.connections.modify.viewmodel.ModifyConnectionViewModel
import eu.wedgess.mihole.ui.navigation.KEY_ARG_ID
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.utils.UiText

@ExperimentalGetImage
fun NavGraphBuilder.ModifyConnectionDestination(
    onComposing: (AppBarState) -> Unit,
    onNavigateBack: () -> Boolean
) {
    composable(
        route = Screens.ModifyConnection.route,
        arguments = listOf(
            navArgument(KEY_ARG_ID) {
                type = NavType.StringType
                nullable = true
            }),
    ) {
        val viewModel: ModifyConnectionViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val currentConnectionId = it.arguments?.getString(KEY_ARG_ID)?.toLong()

        LaunchedEffect(Unit) {
            onComposing(
                AppBarState(
                    title = UiText.StringResource(id = R.string.appbar_title_connection),
                    showNavigateBackIcon = true
                )
            )
            currentConnectionId?.run {
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
            viewModel.barcodeScanner,
            viewModel::onEvent
        )
    }
}