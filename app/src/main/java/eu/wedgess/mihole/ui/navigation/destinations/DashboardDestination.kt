package eu.wedgess.mihole.ui.navigation.destinations

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.dashboard.DashboardContract
import eu.wedgess.mihole.ui.dashboard.view.DashboardScreen
import eu.wedgess.mihole.ui.dashboard.viewmodel.DashboardViewModel
import eu.wedgess.mihole.ui.navigation.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.DashboardDestination(
    onComposing: (AppBarState) -> Unit
) {
    composable(Screens.Dashboard.route) {
        val viewModel: DashboardViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            onComposing(
                AppBarState(
                    title = "Dashboard"
                )
            )
            do {
                viewModel.onEvent(DashboardContract.Event.FetchStatistics)
                viewModel.onEvent(DashboardContract.Event.FetchQueriesOvertime)
                viewModel.onEvent(DashboardContract.Event.FetchClientQueriesOvertime)
                delay(10_000)
            } while (true)
        }

        LaunchedEffect(Unit) {
            viewModel.effect.collectLatest { effect ->

            }
        }

        DashboardScreen(
            uiState,
            viewModel::onEvent
        )
    }
}